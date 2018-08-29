package com.tokopedia.common.network.util;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;

import java.io.StringReader;
import java.lang.reflect.Type;

public class CommonUtil {
    public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        StringReader reader = new StringReader(json);
        T target = (T) new Gson().fromJson(reader, typeOfT);
        return target;
    }

    public static String toJson(Object src) {
        Gson gson = new Gson();
        if (src == null) {
            return gson.toJson(JsonNull.INSTANCE);
        }
        return gson.toJson(src, src.getClass());
    }
}
