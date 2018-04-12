package com.tokopedia.shop.common.util;

import android.text.TextUtils;

/**
 * Created by nathan on 2/21/18.
 */

public class TextApiUtils {

    private static final String BOOLEAN_VALUE_TRUE = "1";
    private static final String BOOLEAN_VALUE_FALSE = "0";

    public static boolean isValueTrue(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        return text.equalsIgnoreCase(BOOLEAN_VALUE_TRUE);
    }

    public static boolean isTextEmpty(String text) {
        if (TextUtils.isEmpty(text)) {
            return true;
        }
        return text.equalsIgnoreCase(BOOLEAN_VALUE_FALSE);
    }
}
