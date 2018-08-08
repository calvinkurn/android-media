package com.tokopedia.abstraction.common.utils.network;

import android.text.TextUtils;

/**
 * Created by nathan on 2/21/18.
 */

public class TextApiUtils {

    private static final String BOOLEAN_VALUE_TRUE = "1";
    private static final String BOOLEAN_VALUE_FALSE = "0";

    public static boolean isValueTrue(String text) {
        return BOOLEAN_VALUE_TRUE.equalsIgnoreCase(text);
    }

    public static boolean isTextEmpty(String text) {
        return TextUtils.isEmpty(text) || BOOLEAN_VALUE_FALSE.equalsIgnoreCase(text);
    }
}
