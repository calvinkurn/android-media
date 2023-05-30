package com.tokopedia.graphql;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.util.CacheHelper;
import com.tokopedia.graphql.util.RemoteConfigHelper;

import java.io.IOException;
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

    public static <T> T fromJson(String json, Type typeOfT, Class<? extends Object> sourceClass) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        StringReader reader = new StringReader(json);
        Gson gson = new GsonBuilder().setLenient().registerTypeAdapterFactory(
                new CheckParseErrorTypeAdapterFactory(sourceClass)
        ).create();
        T target = (T) gson.fromJson(reader, typeOfT);
        return target;
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        return (T) new Gson().fromJson(new JsonTreeReader(json), typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT, Class<? extends Object> sourceClass) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        Gson gson = new GsonBuilder().setLenient().registerTypeAdapterFactory(
                new CheckParseErrorTypeAdapterFactory(sourceClass)
        ).create();
        return (T) gson.fromJson(new JsonTreeReader(json), typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }

        T t = (T) new Gson().fromJson(new JsonTreeReader(json), classOfT);
        return t;
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT, Class<? extends Object> sourceClass) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        Gson gson = new GsonBuilder().setLenient().registerTypeAdapterFactory(
                new CheckParseErrorTypeAdapterFactory(sourceClass)
        ).create();
        return (T) gson.fromJson(new JsonTreeReader(json), classOfT);
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

    static class CheckParseErrorTypeAdapterFactory implements TypeAdapterFactory {
        private final Class<? extends Object> sourceClass;
        public CheckParseErrorTypeAdapterFactory(Class<? extends Object> sourceClass){
            this.sourceClass = sourceClass;
        }
        public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            return createCustomTypeAdapter(delegate, type);
        }

        private <T> TypeAdapter<T> createCustomTypeAdapter(TypeAdapter<T> delegate, TypeToken<T> type) {
            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                }

                @Override
                public T read(JsonReader in) throws IOException {
                    try {
                        return delegate.read(in);
                    } catch (JsonSyntaxException jse) {
                        StackTraceElement matchedStackTraceWithSourceClass = getMatchedStackTrace(jse.getCause().getStackTrace(), sourceClass);
                        String methodSourceValue = matchedStackTraceWithSourceClass.getFileName() + " - " +  matchedStackTraceWithSourceClass.getMethodName();
                        String jsonErrorField = in.getPath();
                        String jsonErrorFieldValueType = in.peek().toString();
                        String responseModel = type.toString();
                        String errorMessage = "Error when parsing model " + 
                                responseModel + 
                                " while reading response on field " + 
                                jsonErrorField + 
                                " with a value type of " + 
                                jsonErrorFieldValueType + 
                                " when processing response on " + 
                                methodSourceValue;
                        throw new JsonSyntaxException(errorMessage, jse.getCause());
                    } catch (Exception exception) {
                        throw exception;
                    }
                }

                private StackTraceElement getMatchedStackTrace(StackTraceElement[] stackTrace, Class<? extends Object> sourceClass) {
                    for (StackTraceElement stackTraceElement : stackTrace) {
                        if (stackTraceElement.getClassName().contains(sourceClass.getSimpleName())) {
                            return stackTraceElement;
                        }
                    }
                    return null;
                }
            };
        }
    }
}

