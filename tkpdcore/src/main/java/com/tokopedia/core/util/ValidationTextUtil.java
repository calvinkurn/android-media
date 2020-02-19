package com.tokopedia.core.util;

/**
 * Created by Angga.Prasetiyo on 10/09/2015.
 */
public class ValidationTextUtil {

    public static boolean isValidText(int minChar, String text) {
        return text.length() >= minChar;
    }

    public static boolean isValidSalesQuery(String query) {
        return isValidText(3, query) && !(query.length()
                <= 3 & query.substring(query.length() - 1).equals(" "));
    }
}
