package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class TokoPointSumCoupon {
    @SerializedName("sumCoupon")
    private int sumCoupon;

    @SerializedName("sumCouponStr")
    private String sumCouponStr;

    public int getSumCoupon() {
        return sumCoupon;
    }

    public void setSumCoupon(int sumCoupon) {
        this.sumCoupon = sumCoupon;
    }

    public String getSumCouponStr() {
        return sumCouponStr;
    }

    public void setSumCouponStr(String sumCouponStr) {
        this.sumCouponStr = sumCouponStr;
    }

    @Override
    public String toString() {
        return "TokoPointSumCoupon{" +
                "sumCoupon=" + sumCoupon +
                ", sumCouponStr='" + sumCouponStr + '\'' +
                '}';
    }
}
