package com.tokopedia.abstraction.common.utils.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ricoharisin on 12/21/15.
 */
public class CacheUtil {

    public static String convertListModelToString(List<?> list, Type type) {
        Gson gson = new Gson();

        JsonElement element = gson.toJsonTree(list, type);

        if (! element.isJsonArray()) {
            throw new RuntimeException();
        }

        return element.getAsJsonArray().toString();
    }

    public static <T> List<T> convertStringToListModel(String jsonArray, Type type) {
        Gson gson = new Gson();

        return (gson.fromJson(jsonArray, type));
    }

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
