package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromoCouponEntity {

    @Expose
    @SerializedName("coupons")
    private List<CouponValueEntity> coupons;

    @Expose
    @SerializedName("has_next")
    private boolean hasNext;

    @Expose
    @SerializedName("total_data")
    private int totalData;

    @Expose
    @SerializedName("extra_info")
    private List<CouponExtraInfoEntity> extraInfo;

    public List<CouponExtraInfoEntity> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(List<CouponExtraInfoEntity> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public List<CouponValueEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponValueEntity> coupons) {
        this.coupons = coupons;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    @Override
    public String toString() {
        return "PromoCouponEntity{" +
                "coupons=" + coupons +
                ", hasNext=" + hasNext +
                ", totalData=" + totalData +
                ", extraInfo=" + extraInfo +
                '}';
    }
}
