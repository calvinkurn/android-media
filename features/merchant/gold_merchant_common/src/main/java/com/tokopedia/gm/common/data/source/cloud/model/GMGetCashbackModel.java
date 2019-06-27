package com.tokopedia.gm.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 10/4/17.
 */

public class GMGetCashbackModel {
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("cashback")
    @Expose
    private int cashback;
    @SerializedName("cashback_amount")
    @Expose
    private int cashbackAmount;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCashback() {
        return cashback;
    }

    public void setCashback(int cashback) {
        this.cashback = cashback;
    }

    public int getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(int cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

}
