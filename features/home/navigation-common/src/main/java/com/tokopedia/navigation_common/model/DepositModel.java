package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class DepositModel {
    @SerializedName("deposit")
    @Expose
    private Double deposit = 0.0;
    @SerializedName("deposit_fmt")
    @Expose
    private String depositFmt = "";

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public String getDepositFmt() {
        return depositFmt;
    }

    public void setDepositFmt(String depositFmt) {
        this.depositFmt = depositFmt;
    }
}
