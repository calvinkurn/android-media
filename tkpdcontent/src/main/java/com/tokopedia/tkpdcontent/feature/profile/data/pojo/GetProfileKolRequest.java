package com.tokopedia.tkpdcontent.feature.profile.data.pojo;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

/**
 * @author by milhamj on 26/02/18.
 */

public class GetProfileKolRequest {
    @SerializedName("query")
    public String query;

    @SerializedName("variables")
    public HashMap<String, Object> variables;

    public GetProfileKolRequest(String query, HashMap<String, Object> variables) {
        this.query = query;
        this.variables = variables;
    }
}
