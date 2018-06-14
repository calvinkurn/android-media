package com.tokopedia.networklib.data.model;

/**
 * Only for internal purposes of this library in order to rewrite the original response
 */
final public class RestResponseInternal {
    private String originalResponse;
    private boolean isCached;
    private int responseCode;

    public RestResponseInternal(String originalResponse, boolean isCached) {
        this.originalResponse = originalResponse;
        this.isCached = isCached;
    }

    public RestResponseInternal(String originalResponse, int responseCode, boolean isCached) {
        this.originalResponse = originalResponse;
        this.isCached = isCached;
    }

    public String getOriginalResponse() {
        return originalResponse;
    }

    public boolean isCached() {
        return isCached;
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String toString() {
        return "RestResponseInternal{" +
                "originalResponse='" + originalResponse + '\'' +
                ", isCached=" + isCached +
                ", responseCode=" + responseCode +
                '}';
    }
}
