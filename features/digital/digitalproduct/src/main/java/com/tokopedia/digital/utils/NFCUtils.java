package com.tokopedia.digital.utils;

import java.math.BigInteger;

/**
 * Created by Rizky on 15/05/18.
 */
public class NFCUtils {

    private static char [] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    public static byte [] hexStringToByteArray(String str) {
        return new BigInteger(str, 16).toByteArray();
    }

    public static String toHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }
}