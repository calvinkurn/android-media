package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class PreValidateRedeemBaseValue {

    @SerializedName("is_valid")
    private int isValid;

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return "PreValidateRedeemBaseValue{" +
                "isValid=" + isValid +
                '}';
    }
}
