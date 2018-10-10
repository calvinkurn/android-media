package com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 31/08/18.
 */
public class ResponseOperator {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("attributes")
    @Expose
    private ResponseOperatorAttributes attributes;

    public String getId() {
        return id;
    }

    public ResponseOperatorAttributes getAttributes() {
        return attributes;
    }

}
