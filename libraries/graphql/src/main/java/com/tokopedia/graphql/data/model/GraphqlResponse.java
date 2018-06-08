package com.tokopedia.graphql.data.model;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Object of this class will serve as response for view lauyer
 */
final public class GraphqlResponse {
    private Map<Type, Object> mResults;
    private final boolean mIsCached;

    public GraphqlResponse(Map<Type, Object> results, boolean isCached) {
        this.mResults = results;
        this.mIsCached = isCached;
    }

    /**
     * @param type Class type (E.g. Xyx.class)
     * @param <T>  Class type of T ( e.g. object of Xyx class)
     * @return Return the object of T
     */
    public final <T> T getData(Type type) {
        return (T) mResults.get(type);
    }

    public boolean isCached() {
        return mIsCached;
    }
}
