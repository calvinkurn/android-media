package com.tokopedia.digital.cart.data.entity.requestbody.atc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/3/17.
 */

public class RequestBodyAtcDigital {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public void setType(String type) {
        this.type = type;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}