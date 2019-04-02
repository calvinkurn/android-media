package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.SerializedName;

public class GamificationSumCouponOuter {
    @SerializedName("tokopointsSumCoupon")
    private GamificationSumCoupon tokopointsSumCoupon;

    public GamificationSumCoupon getTokopointsSumCoupon() {
        return tokopointsSumCoupon;
    }

    public void setTokopointsSumCoupon(GamificationSumCoupon tokopointsSumCoupon) {
        this.tokopointsSumCoupon = tokopointsSumCoupon;
    }

    @Override
    public String toString() {
        return "TokoPointSumCouponOuter{" +
                "tokopointsSumCoupon=" + tokopointsSumCoupon +
                '}';
    }
}
