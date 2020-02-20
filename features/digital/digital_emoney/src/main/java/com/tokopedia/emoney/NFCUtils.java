package com.tokopedia.emoney;

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

    public static String formatCardUID(String cardNumber) {
        return cardNumber.substring(0, 4) + " - " + cardNumber.substring(4, 8) + " - " +
                cardNumber.substring(8, 12) + " - " + cardNumber.substring(12, 16);
    }

    public static String formatCardUIDWithSpace(String cardNumber) {
        return cardNumber.substring(0, 4) + " " + cardNumber.substring(4, 8) + " " +
                cardNumber.substring(8, 12) + " " + cardNumber.substring(12, 16);
    }

    public static String convertCardUID(String cardNumber) {
        return cardNumber.substring(0, 4) + cardNumber.substring(4, 8) +
                cardNumber.substring(8, 12) + cardNumber.substring(12, 16);
    }

    public static int convertCardLastBalance(String lastBalance) {
        String lastBalanceInHex = lastBalance.substring(6,8) +
                lastBalance.substring(4,6) +
                lastBalance.substring(2,4) +
                lastBalance.substring(0,2);
        return Integer.parseInt(lastBalanceInHex,16);
    }

}