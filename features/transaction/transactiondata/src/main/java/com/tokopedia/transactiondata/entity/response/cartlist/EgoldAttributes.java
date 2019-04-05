package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.SerializedName;

public class EgoldAttributes {

    @SerializedName("eligible")
    private boolean isEligible;

    @SerializedName("range")
    private EgoldRange egoldRange;

    @SerializedName("message")
    private EgoldMessage egoldMessage;

    public boolean isEligible() {
        return isEligible;
    }

    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }

    public EgoldRange getEgoldRange() {
        return egoldRange;
    }

    public void setEgoldRange(EgoldRange egoldRange) {
        this.egoldRange = egoldRange;
    }

    public EgoldMessage getEgoldMessage() {
        return egoldMessage;
    }

    public void setEgoldMessage(EgoldMessage egoldMessage) {
        this.egoldMessage = egoldMessage;
    }
}
