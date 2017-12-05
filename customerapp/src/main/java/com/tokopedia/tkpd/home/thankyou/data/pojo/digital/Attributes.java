package com.tokopedia.tkpd.home.thankyou.data.pojo.digital;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

/**
 * Created by okasurya on 12/5/17.
 */

public class Attributes {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("identifier")
    @Expose
    private RequestBodyIdentifier identifier;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public RequestBodyIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(RequestBodyIdentifier identifier) {
        this.identifier = identifier;
    }
}
