package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponseValidateCoupon {
    @SerializedName("data")
    @Expose
    private ValidateCouponBaseEntity data;

    @SerializedName("errors")
    @Expose
    private List<ErrorEntity> errors;

    public ValidateCouponBaseEntity getData() {
        return data;
    }

    public void setData(ValidateCouponBaseEntity data) {
        this.data = data;
    }

    public List<ErrorEntity> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorEntity> errors) {
        this.errors = errors;
    }
}
