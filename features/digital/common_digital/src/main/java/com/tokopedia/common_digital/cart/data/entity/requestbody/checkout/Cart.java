package com.tokopedia.common_digital.cart.data.entity.requestbody.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class Cart {

    @SerializedName("data")
    @Expose
    private Data data;

    public Cart(Data data) {
        this.data = data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
