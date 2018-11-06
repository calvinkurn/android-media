package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class PreValidateRedeemBase {

    @SerializedName("hachikoPreValidateRedeem")
    private PreValidateRedeemBaseValue preValidateRedeem;

    public PreValidateRedeemBaseValue getPreValidateRedeem() {
        return preValidateRedeem;
    }

    public void setPreValidateRedeem(PreValidateRedeemBaseValue preValidateRedeem) {
        this.preValidateRedeem = preValidateRedeem;
    }

    @Override
    public String toString() {
        return "PreValidateRedeemBase{" +
                "preValidateRedeem=" + preValidateRedeem +
                '}';
    }
}
