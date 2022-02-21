package com.tokopedia.network.authentication;

import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class AuthHelperJava {
    // Bitwise operations are still mostly experimental in Kotlin
    protected static String getMD5Hash(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(raw.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();

            for (byte message : messageDigest) {
                hexString.append(String.format("%02x", message & 0xff));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return "";
        }
    }

    protected static String base64Encoder(byte[] inputBytes, int flags) {
        return Base64.encodeToString(inputBytes, flags);
    }

    protected static String base64Encoder(String inputString, int flags) {
        return base64Encoder(inputString.getBytes(), flags);
    }
}
