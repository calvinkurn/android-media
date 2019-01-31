package com.tokopedia.graphql.data.model;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Object of this class will serve as response for view lauyer
 */
public class GraphqlResponse {
    private Map<Type, Object> mResults;
    private Map<Type, List<GraphqlError>> mErrors;
    private final boolean mIsCached;

    public GraphqlResponse(Map<Type, Object> results, Map<Type, List<GraphqlError>> errors, boolean isCached) {
        this.mResults = results;
        this.mErrors = errors;
        this.mIsCached = isCached;
    }

    /**
     * @param type Class type (E.g. Xyx.class)
     * @param <T>  Class type of T ( e.g. object of Xyx class)
     * @return Return the object of T
     */
    public final <T> T getData(Type type) {
        try {
            return (T) mResults.get(type);
        }catch (Exception e){
            return null;
        }
    }

    public final List<GraphqlError> getError(Type type) {
        return mErrors.get(type);
    }

    public boolean isCached() {
        return mIsCached;
    }
}
