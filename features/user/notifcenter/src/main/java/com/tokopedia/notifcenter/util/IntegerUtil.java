package com.tokopedia.notifcenter.util;

/**
 * Created by meta on 15/08/18.
 */
public class IntegerUtil {

    public static int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }
}
