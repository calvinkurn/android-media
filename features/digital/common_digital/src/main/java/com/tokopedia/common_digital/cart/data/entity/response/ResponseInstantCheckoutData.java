package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/23/17.
 */

public class ResponseInstantCheckoutData {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("attributes")
    @Expose
    private AttributesInstantCheckout attributes;

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public AttributesInstantCheckout getAttributes() {
        return attributes;
    }
}
