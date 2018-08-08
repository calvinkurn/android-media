package com.tokopedia.tokocash;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class CacheUtil {

    public static final String KEY_TOKOCASH_BALANCE_CACHE = "TOKOCASH_BALANCE_CACHE";

    public static String convertModelToString(Object obj, Type type) {
        Gson gson = new Gson();

        JsonElement element = gson.toJsonTree(obj, type);

        if (!element.isJsonObject()) {
            throw new RuntimeException();
        }

        return element.getAsJsonObject().toString();
    }

    public static <T> T convertStringToModel(String json, Type type) {
        Gson gson = new Gson();

        return (gson.fromJson(json, type));
    }

}
