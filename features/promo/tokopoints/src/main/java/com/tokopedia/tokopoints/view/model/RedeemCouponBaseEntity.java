package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedeemCouponBaseEntity {

    @Expose
    @SerializedName("hachikoRedeem")
    private RedeemCouponEntity hachikoRedeem;

    public RedeemCouponEntity getHachikoRedeem() {
        return hachikoRedeem;
    }

    public void setHachikoRedeem(RedeemCouponEntity hachikoRedeem) {
        this.hachikoRedeem = hachikoRedeem;
    }

    @Override
    public String toString() {
        return "RedeemCouponBaseEntity{" +
                "hachikoRedeem=" + hachikoRedeem +
                '}';
    }
}
