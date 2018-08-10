package com.tokopedia.shop.common.graphql.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class GraphQLSuccessMessage {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
