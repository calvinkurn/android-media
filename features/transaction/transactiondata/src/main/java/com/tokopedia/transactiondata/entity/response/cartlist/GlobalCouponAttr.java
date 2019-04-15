package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fwidjaja on 14/04/19.
 */
public class GlobalCouponAttr {
    @SerializedName("description")
    private String description;

    @SerializedName("quantity_label")
    private String quantityLabel;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantityLabel() {
        return quantityLabel;
    }

    public void setQuantityLabel(String quantityLabel) {
        this.quantityLabel = quantityLabel;
    }
}
