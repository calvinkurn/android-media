package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EgoldAttributes {

    @SerializedName("eligible")
    private boolean isEligible;

    @SerializedName("is_tiering")
    private boolean isTiering;

    @SerializedName("range")
    private EgoldRange egoldRange = new EgoldRange();

    @SerializedName("message")
    private EgoldMessage egoldMessage = new EgoldMessage();

    @SerializedName("tier_data")
    private ArrayList<EgoldTieringData> egoldTieringDataArrayList = new ArrayList<>();

    public ArrayList<EgoldTieringData> getEgoldTieringDataArrayList() {
        return egoldTieringDataArrayList;
    }

    public void setEgoldTieringDataArrayList(ArrayList<EgoldTieringData> egoldTieringDataArrayList) {
        this.egoldTieringDataArrayList = egoldTieringDataArrayList;
    }

    public boolean isEligible() {
        return isEligible;
    }

    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }

    public boolean isTiering() {
        return isTiering;
    }

    public void setTiering(boolean tiering) {
        isTiering = tiering;
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
