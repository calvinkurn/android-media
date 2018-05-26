package com.tokopedia.oms.view.utils;

import android.util.Log;


public class Utils {
    private static Utils singleInstance;

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
        Log.d("UTILS", "Utils Instance created");
    }

    public static class Constants {
        public final static String CHECKOUTDATA = "checkoutdata";
    }
}
