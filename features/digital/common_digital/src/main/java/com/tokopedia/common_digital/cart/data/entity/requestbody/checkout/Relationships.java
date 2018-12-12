package com.tokopedia.common_digital.cart.data.entity.requestbody.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class Relationships {
    @SerializedName("cart")
    @Expose
    private Cart cart;

    public Relationships(Cart cart) {
        this.cart = cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
