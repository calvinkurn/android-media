package com.tokopedia.transactiondata.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EgoldData {
    @SerializedName("is_egold")
    @Expose
    public boolean isEgold;

    @SerializedName("gold_amount")
    @Expose
    public long egoldAmount;

    public boolean isEgold() {
        return isEgold;
    }

    public void setEgold(boolean egold) {
        isEgold = egold;
    }

    public long getEgoldAmount() {
        return egoldAmount;
    }

    public void setEgoldAmount(long egoldAmount) {
        this.egoldAmount = egoldAmount;
    }
}
