package com.tokopedia.user.session.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncoderDecoder {

    // since it can't use remote config or rollence, we can hansel this function if there's an error in the future
    public static boolean isReturnNull() {
        return false;
    }

    public static String Encrypt(String text, String initialVector) {
        int[] raw = new int[]{103, 103, 103, 103, 116, 116, 116, 116, 116, 117, 106, 107, 114, 114, 114, 114};
        SecretKeySpec skeySpec = new SecretKeySpec(convertRaw(raw).getBytes(), "AES");
        String encode_result = null;
        IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivs);
            byte[] encryptedData = cipher.doFinal(text.getBytes());
            encode_result = Base64_.encodeToString(encryptedData, 0);
            encode_result.replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode_result;
    }

    public static String Encrypt(String text, String initialVector, byte[] raw) {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        String encode_result = null;
        IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivs);
            byte[] encryptedData = cipher.doFinal(text.getBytes());
            encode_result = Base64_.encodeToString(encryptedData, 0);
            encode_result.replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode_result;
    }

    public static String Decrypt(String text, String initialVector, byte[] raw) {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //String initialVector = "abcdefgh";
        String decode_result = isReturnNull() ? null : "";
        IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
        try {
            byte[] data = Base64_.decode(text, Base64_.DEFAULT);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivs);
            byte[] decryptedData = cipher.doFinal(data);
            decode_result = new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decode_result;
    }

    public static String Decrypt(String text, String initialVector) {
        byte[] raw = new byte[]{'g', 'g', 'g', 'g', 't', 't', 't', 't', 't', 'u', 'j', 'k', 'r', 'r', 'r', 'r'};
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //String initialVector = "abcdefgh";
        String decode_result = isReturnNull() ? null : "";
        IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
        try {
            byte[] data = Base64_.decode(text, Base64_.DEFAULT);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivs);
            byte[] decryptedData = cipher.doFinal(data);
            decode_result = new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decode_result;
    }

    private static String convertRaw(int[] raw) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<raw.length;i++) {
            char a = (char) raw[i];
            sb.append(a);
        }
        return sb.toString();
    }

}
