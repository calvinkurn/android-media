package com.tokopedia.posapp.domain.model.bank;

/**
 * Created by okasurya on 9/5/17.
 */

public class InstallmentDomain {
    private int term;
    private float feeValue;
    private String feeType;
    private float interest;
    private float minimumAmount;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public float getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(float feeValue) {
        this.feeValue = feeValue;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    public float getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(float minimumAmount) {
        this.minimumAmount = minimumAmount;
    }
}
