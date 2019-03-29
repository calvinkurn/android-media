package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateCouponBaseEntity {
    @Expose
    @SerializedName("validateRedeem")
    private ValidateCouponEntity validateCoupon;

    public ValidateCouponEntity getValidateCoupon() {
        return validateCoupon;
    }

    public void setValidateCoupon(ValidateCouponEntity validateCoupon) {
        this.validateCoupon = validateCoupon;
    }

    @Override
    public String toString() {
        return "ValidateCouponBaseEntity{" +
                "startValidateCoupon=" + validateCoupon +
                '}';
    }
}
