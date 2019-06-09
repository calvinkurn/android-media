package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.SerializedName;

public class EgoldTieringData {

    @SerializedName("minimum_total_amount")
    private long minToalAmount;

    @SerializedName("minimum_amount")
    private long minAmount;

    @SerializedName("maximum_amount")
    private long maxAmount;

    @SerializedName("basis_amount")
    private long basisAmount;

    public long getMinToalAmount() {;
        return minToalAmount;
    }

    public void setMinToalAmount(long minToalAmount) {
        this.minToalAmount = minToalAmount;
    }

    public long getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(long minAmount) {
        this.minAmount = minAmount;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }

    public long getBasisAmount() {
        return basisAmount;
    }

    public void setBasisAmount(long basisAmount) {
        this.basisAmount = basisAmount;
    }
}
