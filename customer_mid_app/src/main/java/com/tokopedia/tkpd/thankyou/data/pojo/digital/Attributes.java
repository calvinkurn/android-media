package com.tokopedia.tkpd.thankyou.data.pojo.digital;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;

/**
 * Created by okasurya on 12/5/17.
 */

public class Attributes {
    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("identifier")
    @Expose
    private RequestBodyIdentifier identifier;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public RequestBodyIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(RequestBodyIdentifier identifier) {
        this.identifier = identifier;
    }
}
