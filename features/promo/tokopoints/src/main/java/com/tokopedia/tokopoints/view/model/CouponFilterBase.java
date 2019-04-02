package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class CouponFilterBase {
    @SerializedName("filter")
    CouponFilterOuter filter;

    public CouponFilterOuter getFilter() {
        return filter;
    }

    public void setFilter(CouponFilterOuter filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "CouponFilterBase{" +
                "filter=" + filter +
                '}';
    }
}
