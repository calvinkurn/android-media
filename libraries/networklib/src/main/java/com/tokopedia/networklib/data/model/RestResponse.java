package com.tokopedia.networklib.data.model;

import java.lang.reflect.Type;

public class RestResponse {
    private final Object mResult;
    private final boolean mIsCached;
    private final int responseCode;

    public RestResponse(Object result, boolean isCached) {
        this.mResult = result;
        this.mIsCached = isCached;
        responseCode = -1;
    }

    public RestResponse(Object result, int responseCode, boolean isCached) {
        this.mResult = result;
        this.mIsCached = isCached;
        this.responseCode = responseCode;
    }

    /**
     * @param type Class type (E.g. Xyx.class)
     * @param <T>  Class type of T ( e.g. object of Xyx class)
     * @return Return the object of T
     */
    public final <T> T getData(Type type) {
        return (T) mResult;
    }

    public boolean isCached() {
        return mIsCached;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
