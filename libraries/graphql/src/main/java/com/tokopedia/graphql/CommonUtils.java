package com.tokopedia.graphql;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.JsonTreeReader;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlRequest;
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
        T target = (T) new Gson().fromJson(reader, typeOfT);
        return target;
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        return (T) new Gson().fromJson(new JsonTreeReader(json), typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }

        T t = (T) new Gson().fromJson(new JsonTreeReader(json), classOfT);
        return t;
    }

    public static String toJson(Object src) {
        Gson gson = new Gson();
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
                return new Gson().fromJson(rawError, JsonArray.class);
            } catch (Exception ignored) {}
        }
        return new JsonArray();
    }

    public static String getGraphqlUrlAppend(String opName) {
        return "graphql/" + GraphqlClient.moduleName + "/" + opName;
    }

    public static String getFullOperationName(GraphqlRequest request) {
        String operationName;
        if (TextUtils.isEmpty(request.getOperationName())) {
            operationName = CacheHelper.getQueryName(request.getQuery());
        } else {
            operationName = request.getOperationName();
        }
        return GraphqlClient.moduleName + "_" + operationName;
    }
}

