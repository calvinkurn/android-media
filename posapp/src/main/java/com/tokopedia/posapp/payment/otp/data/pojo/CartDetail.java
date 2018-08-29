package com.tokopedia.posapp.payment.otp.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 10/11/17.
 */

public class CartDetail {
    @SerializedName("prod_id")
    @Expose
    private long productId;
    @SerializedName("qty")
    @Expose
    private int qty;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
