package com.tokopedia.cacheapi.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

    private static final byte[] SECRET_KEY = new byte[]{'g', 'g', 'g', 'g', 't', 't', 't', 't', 't', 'u', 'j', 'k', 'r', 'r', 'r', 'r'};
    private static final String CHIPER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static String Encrypt(String text, String initialVector) {
        SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY, "AES");
        String encodedResult = null;
        IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
        try {
            Cipher cipher = Cipher.getInstance(CHIPER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivs);
            byte[] encryptedData = cipher.doFinal(text.getBytes());
            encodedResult = Base64.encodeToString(encryptedData, 0);
            encodedResult = encodedResult.replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedResult;
    }

    public static String Decrypt(String text, String initialVector) {
        SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY, "AES");
        String decodedResult = null;
        IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
        try {
            byte[] data = Base64.decode(text, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(CHIPER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivs);
            byte[] decryptedData = cipher.doFinal(data);
            decodedResult = new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedResult;
    }

}
