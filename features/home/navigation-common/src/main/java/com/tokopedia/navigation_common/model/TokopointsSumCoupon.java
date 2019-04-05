package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 8/30/18.
 */
public class TokopointsSumCoupon {
    @SerializedName("sumCoupon")
    @Expose
    private int sumCoupon = 0;
    @SerializedName("sumCouponStr")
    @Expose
    private String sumCouponStr = "";

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
}
