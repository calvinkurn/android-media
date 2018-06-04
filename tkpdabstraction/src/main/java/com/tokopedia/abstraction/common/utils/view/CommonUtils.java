package com.tokopedia.abstraction.common.utils.view;

import android.util.Log;

import com.tokopedia.abstraction.common.utils.GlobalConfig;

public class CommonUtils {

    public static void dumper(Object o) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            Log.i("Dumper", o.toString());
        }
    }

    public static void dumper(String str) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            Log.i("Dumper", str);
        }
    }

    public static <T> boolean checkStringNotNull(T reference) {
        return checkNotNull(reference) && (!reference.equals("")) && (!reference.equals("0"));
    }

    public static <T> boolean checkNotNull(T reference) {
        return reference != null;
    }

}
