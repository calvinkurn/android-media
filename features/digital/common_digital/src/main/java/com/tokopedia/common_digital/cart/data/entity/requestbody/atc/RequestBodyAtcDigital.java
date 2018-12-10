package com.tokopedia.common_digital.cart.data.entity.requestbody.atc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 27/08/18.
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
