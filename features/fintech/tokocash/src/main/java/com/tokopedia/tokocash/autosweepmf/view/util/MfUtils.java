package com.tokopedia.tokocash.autosweepmf.view.util;

public class MfUtils {
    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.equalsIgnoreCase("null") || string.isEmpty());
    }
}
