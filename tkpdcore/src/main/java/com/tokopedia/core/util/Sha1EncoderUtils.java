package com.tokopedia.core.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by kris on 10/12/17. Tokopedia
 */

public class Sha1EncoderUtils {

    private static final String MAC_ALGORITHM = "HmacSHA1";

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    private static String calculateRFC2104HMAC(String authString, String authKey) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(authKey.getBytes(), MAC_ALGORITHM);
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(authString.getBytes());
            return convertToHex(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getRFC2104HMAC(String authString, String authKey) {
        return calculateRFC2104HMAC(authString, authKey);
    }

}
