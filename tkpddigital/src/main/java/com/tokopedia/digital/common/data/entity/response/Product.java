package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class Product {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private AttributesProduct attributes;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public AttributesProduct getAttributes() {
        return attributes;
    }
}
