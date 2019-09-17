package com.tokopedia.authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthHelperJava {
    // Bitwise operations are still mostly experimental in Kotlin
    public static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
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
}
