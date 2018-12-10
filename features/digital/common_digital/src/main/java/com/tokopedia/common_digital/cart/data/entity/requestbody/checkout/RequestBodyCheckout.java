package com.tokopedia.common_digital.cart.data.entity.requestbody.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class RequestBodyCheckout {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("relationships")
    @Expose
    private Relationships relationships;

    public void setType(String type) {
        this.type = type;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setRelationships(Relationships relationships) {
        this.relationships = relationships;
    }
}
