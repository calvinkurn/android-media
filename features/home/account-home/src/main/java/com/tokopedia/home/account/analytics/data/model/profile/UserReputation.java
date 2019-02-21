
package com.tokopedia.home.account.analytics.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReputation {

    @SerializedName("positive_percentage")
    @Expose
    private String positivePercentage = "";
    @SerializedName("no_reputation")
    @Expose
    private Integer noReputation = 0;
    @SerializedName("negative")
    @Expose
    private String negative = "";
    @SerializedName("positive")
    @Expose
    private String positive = "";
    @SerializedName("neutral")
    @Expose
    private String neutral = "";

    public String getPositivePercentage() {
        return positivePercentage;
    }

    public void setPositivePercentage(String positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    public Integer getNoReputation() {
        return noReputation;
    }

    public void setNoReputation(Integer noReputation) {
        this.noReputation = noReputation;
    }

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public String getNeutral() {
        return neutral;
    }

    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }
}
