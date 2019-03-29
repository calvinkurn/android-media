package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class CouponDetailOuter {

    @SerializedName("detail")
    private CouponValueEntity detail;

    public CouponValueEntity getDetail() {
        return detail;
    }

    public void setDetail(CouponValueEntity detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "CouponDetailOuter{" +
                "detail=" + detail +
                '}';
    }
}
