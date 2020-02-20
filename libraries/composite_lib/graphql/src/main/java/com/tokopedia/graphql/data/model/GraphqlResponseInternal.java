package com.tokopedia.graphql.data.model;

import com.google.gson.JsonArray;

import java.util.ArrayList;

/**
 * Only for internal purposes of this library in order to rewrite the original response
 */
final public class GraphqlResponseInternal {
    private JsonArray originalResponse;
    private boolean isCached;
    private ArrayList<Integer> indexOfEmptyCached;
    private String mBeCache;

    public GraphqlResponseInternal(JsonArray originalResponse, boolean isCached) {
        this.originalResponse = originalResponse;
        this.isCached = isCached;
    }

    public GraphqlResponseInternal(JsonArray originalResponse, boolean isCached, ArrayList<Integer> indexOfEmptyCached) {
        this(originalResponse, isCached);
        this.indexOfEmptyCached = indexOfEmptyCached;
    }

    public GraphqlResponseInternal(JsonArray originalResponse, boolean isCached, String beCache) {
        this(originalResponse, isCached);
        this.mBeCache = beCache;
    }

    public ArrayList<Integer> getIndexOfEmptyCached() {
        return indexOfEmptyCached;
    }

    public JsonArray getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(JsonArray originalResponse) {
        this.originalResponse = originalResponse;
    }

    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean isCached) {
        this.isCached = isCached;
    }

    public String getBeCache() {
        return this.mBeCache;
    }

    public void setBeCache(String mBeCache) {
        this.mBeCache = mBeCache;
    }

    @Override
    public String toString() {
        return "GraphqlResponseInternal{" +
                "originalResponse=" + originalResponse +
                ", isCached=" + isCached +
                ", indexOfEmptyCached=" + indexOfEmptyCached +
                ", mBeCache='" + mBeCache + '\'' +
                '}';
    }
}
