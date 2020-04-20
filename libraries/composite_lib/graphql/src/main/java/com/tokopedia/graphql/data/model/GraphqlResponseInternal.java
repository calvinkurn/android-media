package com.tokopedia.graphql.data.model;

import com.google.gson.JsonArray;

import java.util.ArrayList;

/**
 * Only for internal purposes of this library in order to rewrite the original response
 */
final public class GraphqlResponseInternal {
    private JsonArray originalResponse;
    // isCached = true only if all of the gql is retrived from database.
    // If there is a response coming from network, isCached should be false.
    private boolean isCached;
    private ArrayList<Integer> indexOfEmptyCached;

    public GraphqlResponseInternal(JsonArray originalResponse, boolean isCached) {
        this.originalResponse = originalResponse;
        this.isCached = isCached;
    }

    public GraphqlResponseInternal(JsonArray originalResponse, ArrayList<Integer> indexOfEmptyCached) {
        this(originalResponse, indexOfEmptyCached == null || indexOfEmptyCached.size() == 0);
        this.indexOfEmptyCached = indexOfEmptyCached;
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

    @Override
    public String toString() {
        return "GraphqlResponseInternal{" +
                "originalResponse=" + originalResponse +
                ", isCached=" + isCached +
                '}';
    }
}
