package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplyCouponBaseEntity {
    @Expose
    @SerializedName("apply_coupon")
    private ApplyCouponEntity applyCoupon;

    @Expose
    @SerializedName("errors")
    private ErrorEntity errors;

    public ApplyCouponEntity getApplyCoupon() {
        return applyCoupon;
    }

    public void setApplyCoupon(ApplyCouponEntity applyCoupon) {
        this.applyCoupon = applyCoupon;
    }

    public ErrorEntity getErrors() {
        return errors;
    }

    public void setErrors(ErrorEntity errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ApplyCouponBaseEntity{" +
                "applyCoupon=" + applyCoupon +
                ", errors=" + errors +
                '}';
    }

}
