package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class TokoPointSumCouponOuter {

    @SerializedName("tokopointsSumNewCoupon")
    private TokoPointSumCoupon tokopointsSumCoupon;

    public TokoPointSumCoupon getTokopointsSumCoupon() {
        return tokopointsSumCoupon;
    }

    public void setTokopointsSumCoupon(TokoPointSumCoupon tokopointsSumCoupon) {
        this.tokopointsSumCoupon = tokopointsSumCoupon;
    }

    @Override
    public String toString() {
        return "TokoPointSumCouponOuter{" +
                "tokopointsSumCoupon=" + tokopointsSumCoupon +
                '}';
    }
}
