
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReputation {

    @SerializedName("positive")
    @Expose
    private Integer positive;
    @SerializedName("neutral")
    @Expose
    private Integer neutral;
    @SerializedName("negative")
    @Expose
    private Integer negative;
    @SerializedName("positive_percentage")
    @Expose
    private String positivePercentage;
    @SerializedName("no_reputation")
    @Expose
    private Integer noReputation;

    public Integer getPositive() {
        return positive;
    }

    public void setPositive(Integer positive) {
        this.positive = positive;
    }

    public Integer getNeutral() {
        return neutral;
    }

    public void setNeutral(Integer neutral) {
        this.neutral = neutral;
    }

    public Integer getNegative() {
        return negative;
    }

    public void setNegative(Integer negative) {
        this.negative = negative;
    }

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

}
