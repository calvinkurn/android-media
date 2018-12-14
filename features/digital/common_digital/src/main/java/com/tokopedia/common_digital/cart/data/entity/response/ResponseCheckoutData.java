package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class ResponseCheckoutData {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("attributes")
    @Expose
    private AttributesCheckout attributes;

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public AttributesCheckout getAttributes() {
        return attributes;
    }
}
