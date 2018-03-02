package com.tokopedia.posapp.bank.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 9/8/17.
 */

public class InstallmentResponse {
    @SerializedName("term")
    @Expose
    private int term;

    @SerializedName("fee_value")
    @Expose
    private double feeValue;

    @SerializedName("fee_type")
    @Expose
    private String feeType;

    @SerializedName("interest")
    @Expose
    private double interest;

    @SerializedName("minimum_ammount")
    @Expose
    private double minimumAmount;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public double getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(double feeValue) {
        this.feeValue = feeValue;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }
}
