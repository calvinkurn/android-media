package com.tokopedia.shop.common.graphql.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class GraphQLResult<T> {
    @SerializedName("result")
    @Expose
    private T result;
    @SerializedName("error")
    @Expose
    private GraphQLDataError graphQLDataError;

    public T getResult() {
        return result;
    }

    public GraphQLDataError getGraphQLDataError() {
        return graphQLDataError;
    }
}
