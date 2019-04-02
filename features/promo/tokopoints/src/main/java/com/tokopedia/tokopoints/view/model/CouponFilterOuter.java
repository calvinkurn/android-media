package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouponFilterOuter {
    @SerializedName("resultStatus")
    private ResultStatusEntity resultStatus;

    @SerializedName("categories")
    private List<CouponFilterItem> categories;

    public ResultStatusEntity getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatusEntity resultStatus) {
        this.resultStatus = resultStatus;
    }

    public List<CouponFilterItem> getCategories() {
        return categories;
    }

    public void setCategories(List<CouponFilterItem> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CouponFilterOuter{" +
                "resultStatus=" + resultStatus +
                ", categories=" + categories +
                '}';
    }
}
