package com.tokopedia.graphql;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.JsonTreeReader;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.gsoncadapter.DoubleSafeTypeAdapter;
import com.tokopedia.graphql.gsoncadapter.FloatSafeTypeAdapter;
import com.tokopedia.graphql.gsoncadapter.IntSafeTypeAdapter;
import com.tokopedia.graphql.gsoncadapter.LongSafeTypeAdapter;
import com.tokopedia.graphql.util.CacheHelper;

import java.io.StringReader;
import java.lang.reflect.Type;

import retrofit2.Response;

public class CommonUtils {
    public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }

        StringReader reader = new StringReader(json);
        T target = (T) getGson().fromJson(reader, typeOfT);
        return target;
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        return (T) getGson().fromJson(new JsonTreeReader(json), typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }

        T t = (T) getGson().fromJson(new JsonTreeReader(json), classOfT);
        return t;
    }

    public static String toJson(Object src) {
        Gson gson = getGson();
        if (src == null) {
            return gson.toJson(JsonNull.INSTANCE);
        }
        return gson.toJson(src, src.getClass());
    }

    public static JsonArray getOriginalResponse(Response<JsonArray> response) {
        if (response.body() != null) return response.body();
        if (response.errorBody() != null) {
            try {
                String rawError = response.errorBody().string();
                return getGson().fromJson(rawError, JsonArray.class);
            } catch (Exception ignored) {
            }
        }
        return new JsonArray();
    }

    public static String getGraphqlUrlAppend(String opName) {
        return "graphql/" + GraphqlClient.moduleName + "/" + opName;
    }

    public static String getFullOperationName(GraphqlRequest request) {
        StringBuilder fullOperationName = new StringBuilder(GraphqlClient.moduleName);
        String operationName;
        fullOperationName.append("_");
        if (TextUtils.isEmpty(request.getOperationName())) {
            operationName = CacheHelper.getQueryName(request.getQuery());
        } else {
            operationName = request.getOperationName();
        }
        fullOperationName.append(operationName);
        return fullOperationName.toString();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        /* Adding type safe adapter for handling
          invalid number in place of Number data type
          e.g Int, Float, Double & long
          Added two to support primitive data types as well.
         */
        gsonBuilder.registerTypeAdapter(int.class, new IntSafeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Integer.class, new IntSafeTypeAdapter());
        gsonBuilder.registerTypeAdapter(long.class, new IntSafeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Long.class, new LongSafeTypeAdapter());
        gsonBuilder.registerTypeAdapter(double.class, new DoubleSafeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Double.class, new DoubleSafeTypeAdapter());
        gsonBuilder.registerTypeAdapter(float.class, new FloatSafeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Float.class, new FloatSafeTypeAdapter());
        return gsonBuilder.create();
    }
}
