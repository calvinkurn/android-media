package com.tokopedia.shop.common.graphql.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class GraphQLDataError {
    @SerializedName("message")
    @Expose
    String message;

    public String getMessage() {
        return message;
    }
}
