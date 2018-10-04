package com.tokopedia.core.referral.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoBenefit {

    @SerializedName("current_benefit")
    @Expose
    private int currentBenefit;

    @SerializedName("max_benefit")
    @Expose
    private int maxBenefit;


    public int getCurrentBenefit() {
        return currentBenefit;
    }

    public void setCurrentBenefit(int currentBenefit) {
        this.currentBenefit = currentBenefit;
    }

    public int getMaxBenefit() {
        return maxBenefit;
    }

    public void setMaxBenefit(int maxBenefit) {
        this.maxBenefit = maxBenefit;
    }
}
