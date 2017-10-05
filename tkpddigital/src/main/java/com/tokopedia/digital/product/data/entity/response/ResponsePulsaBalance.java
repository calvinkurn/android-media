package com.tokopedia.digital.product.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ashwanityagi on 04/07/17.
 */

public class ResponsePulsaBalance {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private AttributesPulsaBalance attributes;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public AttributesPulsaBalance getAttributes() {
        return attributes;
    }
}