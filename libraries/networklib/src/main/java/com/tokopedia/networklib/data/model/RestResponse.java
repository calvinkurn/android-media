package com.tokopedia.networklib.data.model;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

public class RestResponse {
    private final Object result;
    private final boolean isCached;
    private final int code;
    private String errorBody;
    private boolean isError;
    private Type type;

    public RestResponse(@NonNull Object result, int code, boolean isCached) {
        this.result = result;
        this.isCached = isCached;
        this.code = code;
    }

    /**
     * @param type Class type (E.g. Xyx.class)
     * @param <T>  Class type of T ( e.g. object of Xyx class)
     * @return Return the object of T
     */
    public final <T> T getData(Type type) {
        return (T) result;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
