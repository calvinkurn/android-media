package com.tokopedia.oms.view.utils;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
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

    public static String transform(JsonObject entity) {
        StringBuffer stringBuffer = new StringBuffer();
        boolean isFirstKey = true;

        try {
            if (entity != null) {
                Set<Map.Entry<String, JsonElement>> set = entity.entrySet();
                for (Map.Entry<String, JsonElement> entry : set) {
                    String key = entry.getKey();
                    JsonElement value = entry.getValue();

                    if (entry.getValue().isJsonArray()) {
                        JsonArray array = value.getAsJsonArray();
                        int size = array.size();
                        for (int index = 0; index < size; index++) {
                            if (!isFirstKey) {
                                stringBuffer.append("&");
                            } else {
                                isFirstKey = false;
                            }
                            stringBuffer.append(URLEncoder.encode(key, "UTF-8"));
                            stringBuffer.append("=");
                            stringBuffer.append(URLEncoder.encode(array.get(index).getAsString(), "UTF-8"));
                        }
                    } else {
                        if (!isFirstKey) {
                            stringBuffer.append("&");
                        } else {
                            isFirstKey = false;
                        }
                        stringBuffer.append(URLEncoder.encode(key, "UTF-8"));
                        stringBuffer.append("=");
                        stringBuffer.append(URLEncoder.encode(value.getAsString(), "UTF-8"));
                    }
                }
            }
        } catch (Exception ex) {

        }

        return stringBuffer.toString();
    }
}
