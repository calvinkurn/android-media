package com.tokopedia.abstraction.common.data.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * @author by milhamj on 26/02/18.
 */

public class GraphqlRequest {
    @SerializedName("query")
    public String query;

    @SerializedName("variables")
    public HashMap<String, Object> variables;

    public GraphqlRequest(String query, HashMap<String, Object> variables) {
        this.query = query;
        this.variables = variables;
    }
}
