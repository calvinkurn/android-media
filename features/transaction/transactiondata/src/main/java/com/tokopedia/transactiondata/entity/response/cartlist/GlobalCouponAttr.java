package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fwidjaja on 14/04/19.
 */
public class GlobalCouponAttr {
    @SerializedName("description")
    private String description;

    @SerializedName("quantity")
    private int quantity;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
