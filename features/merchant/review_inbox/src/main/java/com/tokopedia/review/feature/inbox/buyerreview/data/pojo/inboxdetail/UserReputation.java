
package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReputation {

    @SerializedName("positive")
    @Expose
    private int positive;
    @SerializedName("neutral")
    @Expose
    private int neutral;
    @SerializedName("negative")
    @Expose
    private int negative;
    @SerializedName("positive_percentage")
    @Expose
    private String positivePercentage;
    @SerializedName("no_reputation")
    @Expose
    private int noReputation;

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getNeutral() {
        return neutral;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

    public String getPositivePercentage() {
        return positivePercentage;
    }

    public void setPositivePercentage(String positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    public int getNoReputation() {
        return noReputation;
    }

    public void setNoReputation(int noReputation) {
        this.noReputation = noReputation;
    }

}
