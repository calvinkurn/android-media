package com.tokopedia.logisticaddaddress.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class LocalDatabaseUtils {

    public static String convertModelToString(Object obj, Type type) {
        Gson gson = new Gson();

        JsonElement element = gson.toJsonTree(obj, type);

        if (! element.isJsonObject()) {
            throw new RuntimeException();
        }

        return element.getAsJsonObject().toString();
    }

    public static <T> T convertStringToModel(String json, Type type) {
        Gson gson = new Gson();

        return (gson.fromJson(json, type));
    }
}
