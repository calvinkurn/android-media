package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class ResponseLastOrderData {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private AttributesLastOrderData attributes;

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public AttributesLastOrderData getAttributes() {
        return attributes;
    }
}
