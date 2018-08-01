package com.tokopedia.oms.data.entity.response.verifyresponse;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class VerifyMyCartResponse {

    @SerializedName("cart")
    private JsonObject cart;

    @SerializedName("status")
    private Status status;

    public JsonObject getCart() {
        return cart;
    }

    public void setCart(JsonObject cart) {
        this.cart = cart;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "VerifyMyCartResponse{" +
                        "cart = '" + cart + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
