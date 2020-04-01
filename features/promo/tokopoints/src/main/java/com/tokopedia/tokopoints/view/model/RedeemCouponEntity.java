package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedeemCouponEntity {
    @Expose
    @SerializedName("coupons")
    private List<CouponDetailEntity> coupons;

    public List<CouponDetailEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponDetailEntity> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "RedeemCouponEntity{" +
                "coupons=" + coupons +
                '}';
    }
}
