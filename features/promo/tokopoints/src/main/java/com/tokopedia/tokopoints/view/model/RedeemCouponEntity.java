package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedeemCouponEntity {
    @Expose
    @SerializedName("coupons")
    private List<CouponDetailEntity> coupons;

    @Expose
    @SerializedName("reward_points")
    private long rewardPoints;

    public List<CouponDetailEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponDetailEntity> coupons) {
        this.coupons = coupons;
    }

    public long getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(long rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    @Override
    public String toString() {
        return "RedeemCouponEntity{" +
                "coupons=" + coupons +
                ", rewardPoints=" + rewardPoints +
                '}';
    }
}
