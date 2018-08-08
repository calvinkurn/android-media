package com.tokopedia.digital.product.data.entity.requestbody.pulsabalance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ashwanityagi on 04/07/17.
 */

public class RequestBodyPulsaBalance {
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