package com.tokopedia.shop.common.graphql.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class GraphQLResult<T> {
    @SerializedName("result")
    @Expose
    T result;
    @SerializedName("error")
    @Expose
    GraphQLDataError graphQLDataError;

    public T getResult() {
        return result;
    }

    public GraphQLDataError getGraphQLDataError() {
        return graphQLDataError;
    }
}
