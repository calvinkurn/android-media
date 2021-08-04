package com.tokopedia.graphql.data.model;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Object of this class will serve as response for view lauyer
 */
public class GraphqlResponse {
    private Map<Type, Object> mResults;
    private Map<Type, Boolean> mIsCachedData;
    private Map<Type, List<GraphqlError>> mErrors;
    private final boolean mIsCached;
    private List<GraphqlRequest> mRefreshRequests;
    private int httpStatusCode = 0;

    public GraphqlResponse(Map<Type, Object> results, Map<Type, List<GraphqlError>> errors, boolean isCached) {
        this.mResults = results;
        this.mErrors = errors;
        this.mIsCached = isCached;
    }

    public GraphqlResponse(Map<Type, Object> results, Map<Type, List<GraphqlError>> errors, Map<Type, Boolean> isCachedData) {
        this.mResults = results;
        this.mErrors = errors;
        this.mIsCachedData = isCachedData;
        this.mIsCached = false;
    }

    /**
     * @param type Class type (E.g. Xyx.class)
     * @param <T>  Class type of T ( e.g. object of Xyx class)
     * @return Return the object of T
     */
    public final <T> T getData(Type type) {
        try {
            return (T) mResults.get(type);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param type Class type (E.g. Xyx.class)
     * @return Return true if data is serve from cache.
     */
    public final boolean isCached(Type type) {
        return mIsCachedData.get(type) == null ? false : mIsCachedData.get(type);
    }

    public final List<GraphqlError> getError(Type type) {
        return mErrors.get(type);
    }

    public boolean isCached() {
        return mIsCached;
    }

    public List<GraphqlRequest> getRefreshRequests() {
        return this.mRefreshRequests;
    }

    public void setRefreshRequests(List<GraphqlRequest> mRefreshRequests) {
        this.mRefreshRequests = mRefreshRequests;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
