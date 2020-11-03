package com.tokopedia.tkpd.thankyou.data.pojo.digital.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ecommerce {
    @SerializedName("purchase")
    @Expose
    private Purchase purchase;

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
}