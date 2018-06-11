package com.tokopedia.networklib.data.model;

import java.lang.reflect.Type;

public class RestResponse {
    private Object mResult;
    private final boolean mIsCached;

    public RestResponse(Object result, boolean isCached) {
        this.mResult = result;
        this.mIsCached = isCached;
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
}
