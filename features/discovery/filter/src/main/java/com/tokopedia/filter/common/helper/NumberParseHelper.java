package com.tokopedia.filter.common.helper;

public class NumberParseHelper {
    public static int safeParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
