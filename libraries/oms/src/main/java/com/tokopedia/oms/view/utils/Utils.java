package com.tokopedia.oms.view.utils;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Set;


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

    public static Bundle transform(JsonObject entity) {
        Bundle bundle = new Bundle();

        if (entity != null) {
            Set<Map.Entry<String, JsonElement>> set = entity.entrySet();
            for (Map.Entry<String, JsonElement> entry : set) {
                bundle.putString(entry.getKey(), entry.getValue().getAsString());
            }
        }

        return bundle;
    }

}
