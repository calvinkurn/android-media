package com.tokopedia.networklib.data.model;

import android.support.annotation.StringRes;

/**
 * Only for internal purposes of this library in order to rewrite the original response
 */
final public class RestResponseInternal {
    private Object originalResponse;
    private boolean isCached;

    public RestResponseInternal(StringRes originalResponse, boolean isCached) {
        this.originalResponse = originalResponse;
        this.isCached = isCached;
    }

    public Object getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(Object originalResponse) {
        this.originalResponse = originalResponse;
    }

    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean isCached) {
        this.isCached = isCached;
    }

    @Override
    public String toString() {
        return "RestResponseInternal{" +
                "originalResponse=" + originalResponse +
                ", isCached=" + isCached +
                '}';
    }
}
