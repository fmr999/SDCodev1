package com.example.android.sdcodev1.AES;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPublicKeySpec;

import javax.crypto.KeyAgreement;

import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.spongycastle.jce.spec.ECNamedCurveParameterSpec;
public class ECDH {

        final protected static char[] hexArray = "0123456789abcdef".toCharArray();
        public static String bytesToHex(byte[] bytes) {
            char[] hexChars = new char[bytes.length * 2];
            for ( int j = 0; j < bytes.length; j++ ) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }

        public static byte [] savePublicKey (PublicKey key) throws Exception
        {

            BCECPublicKey eckey = (BCECPublicKey)key;
            //System.out.println(eckey.getFormat());
            byte [] Pukey= eckey.getQ().getEncoded();

            return Pukey;

        }

        public static PublicKey loadPublicKey (byte [] data) throws Exception
        {
		/*KeyFactory kf = KeyFactory.getInstance("ECDH", "SC");
		return kf.generatePublic(new X509EncodedKeySpec(data));*/

            ECNamedCurveParameterSpec params = org.spongycastle.jce.ECNamedCurveTable.getParameterSpec("secp384r1");//Change to 128 currently 192
            org.spongycastle.jce.spec.ECPublicKeySpec pubKey = new org.spongycastle.jce.spec.ECPublicKeySpec(
                    params.getCurve().decodePoint(data), params);
            KeyFactory kf = KeyFactory.getInstance("ECDH", "SC");

            //System.out.println("In ECDH class"+kf.generatePublic(pubKey));
            return kf.generatePublic(pubKey);
        }

        public static byte [] savePrivateKey (PrivateKey key) throws Exception
        {
            //return key.getEncoded();

            BCECPrivateKey eckey = (BCECPrivateKey)key;
            return eckey.getD().toByteArray();
        }

        public static PrivateKey loadPrivateKey (byte [] data) throws Exception
        {
            //KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
            //return kf.generatePrivate(new PKCS8EncodedKeySpec(data));

            ECNamedCurveParameterSpec params = org.spongycastle.jce.ECNamedCurveTable.getParameterSpec("secp384r1");
            org.spongycastle.jce.spec.ECPrivateKeySpec prvkey = new org.spongycastle.jce.spec.ECPrivateKeySpec(new BigInteger(data), params);
            KeyFactory kf = KeyFactory.getInstance("ECDH", "SC");
            return kf.generatePrivate(prvkey);
        }

        public static String doECDH (String name, byte[] dataPrv, byte[] dataPub) throws Exception
        {
            KeyAgreement KeyAgree = KeyAgreement.getInstance("ECDH", "SC");
            KeyAgree.init(loadPrivateKey(dataPrv));
            KeyAgree.doPhase(loadPublicKey(dataPub), true);
            byte [] secret = KeyAgree.generateSecret();
            String a=bytesToHex(secret);
            System.out.println(name + bytesToHex(secret));
            return a;
        }


    }
