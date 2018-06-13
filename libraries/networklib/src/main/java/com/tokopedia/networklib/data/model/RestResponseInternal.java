package com.tokopedia.networklib.data.model;

/**
 * Only for internal purposes of this library in order to rewrite the original response
 */
final public class RestResponseInternal {
    private String originalResponse;
    private boolean isCached;

    public RestResponseInternal(String originalResponse, boolean isCached) {
        this.originalResponse = originalResponse;
        this.isCached = isCached;
    }

    public String getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(String originalResponse) {
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
