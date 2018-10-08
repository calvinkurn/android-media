package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class CouponSwipeUpdateOuter {

    @SerializedName("tokopointsSwipeCoupon")
    private CouponSwipeUpdate swipeCoupon;

    public CouponSwipeUpdate getSwipeCoupon() {
        return swipeCoupon;
    }

    public void setSwipeCoupon(CouponSwipeUpdate swipeCoupon) {
        this.swipeCoupon = swipeCoupon;
    }

    @Override
    public String toString() {
        return "CouponSwipeUpdateOuter{" +
                "swipeCoupon=" + swipeCoupon +
                '}';
    }
}
