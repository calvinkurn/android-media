package com.tokopedia.digital.utils;

/**
 * Created by Rizky on 15/05/18.
 */
public class NFCUtils {

    public static String formatCardUIDWithSpace(String cardNumber) {
        return cardNumber.substring(0, 4) + " " + cardNumber.substring(4, 8) + " " +
                cardNumber.substring(8, 12) + " " + cardNumber.substring(12, 16);
    }

}