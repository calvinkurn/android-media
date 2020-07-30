package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class TokoPointSumCoupon {

    @SerializedName("sumNewCoupon")
    private int sumCoupon;

    @SerializedName("sumNewCouponStr")
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
