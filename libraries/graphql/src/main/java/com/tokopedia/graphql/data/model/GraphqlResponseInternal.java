package com.tokopedia.graphql.data.model;

import com.google.gson.JsonArray;

import java.util.ArrayList;

/**
 * Only for internal purposes of this library in order to rewrite the original response
 */
final public class GraphqlResponseInternal {
    private JsonArray originalResponse ;
    // isCached = true only if all of the gql is retrived from database.
    // If there is a response coming from network, isCached should be false.
    private boolean isCached;
    private ArrayList<Integer> indexOfEmptyCached;
    private String mBeCache;
    private String queryHash;

    public GraphqlResponseInternal(JsonArray originalResponse, boolean isCached) {
        this.originalResponse = originalResponse;
        this.isCached = isCached;
    }

    public GraphqlResponseInternal(JsonArray originalResponse, ArrayList<Integer> indexOfEmptyCached) {
        this(originalResponse, indexOfEmptyCached == null || indexOfEmptyCached.size() == 0);
        this.indexOfEmptyCached = indexOfEmptyCached;
    }

    public GraphqlResponseInternal(JsonArray originalResponse, boolean isCached, String beCache) {
        this(originalResponse, isCached);
        this.mBeCache = beCache;
    }

    public GraphqlResponseInternal(JsonArray originalResponse, boolean isCached, String beCache, String queryHashHeader) {
        this(originalResponse, isCached);
        this.mBeCache = beCache;
        this.queryHash = queryHashHeader;
    }

    public ArrayList<Integer> getIndexOfEmptyCached() {
        return indexOfEmptyCached;
    }

    public JsonArray getOriginalResponse() {
        return originalResponse;
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

    public String getQueryHash() {
        return queryHash;
    }

    public void setQueryHash(String queryHash) {
        this.queryHash = queryHash;
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
