package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.SerializedName;

public class EgoldRange {

    @SerializedName("min")
    private int minEgoldValue;

    @SerializedName("max")
    private int maxEgoldValue;

    public int getMinEgoldValue() {
        return minEgoldValue;
    }

    public void setMinEgoldValue(int minEgoldValue) {
        this.minEgoldValue = minEgoldValue;
    }

    public int getMaxEgoldValue() {
        return maxEgoldValue;
    }

    public void setMaxEgoldValue(int maxEgoldValue) {
        this.maxEgoldValue = maxEgoldValue;
    }
}
