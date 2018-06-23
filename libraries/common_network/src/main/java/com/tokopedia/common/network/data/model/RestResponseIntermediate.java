package com.tokopedia.common.network.data.model;

import java.lang.reflect.Type;

/**
 * Only for internal purposes of this library in order to rewrite the original response
 */
final public class RestResponseIntermediate {
    final private Object data;
    private String errorBody;
    final private boolean isCached;
    final private Type type;
    private int code;
    private boolean isError;

    public RestResponseIntermediate(Object data, Type type, boolean isCached) {
        this.data = data;
        this.type = type;
        this.isCached = isCached;
    }

    public Object getOriginalResponse() {
        return data;
    }

    public boolean isCached() {
        return isCached;
    }

    public int getCode() {
        return code;
    }

    public Type getType() {
        return type;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }

    @Override
    public String toString() {
        return "RestResponseIntermediate{" +
                "originalResponse='" + data + '\'' +
                ", isCached=" + isCached +
                ", code=" + code +
                ", isError=" + isError +
                '}';
    }
}
