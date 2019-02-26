package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class PointsModel {
    @SerializedName("reward")
    @Expose
    private Integer reward = 0;
    @SerializedName("rewardStr")
    @Expose
    private String rewardStr = "";
    @SerializedName("loyalty")
    @Expose
    private Integer loyalty = 0;
    @SerializedName("loyaltyStr")
    @Expose
    private String loyaltyStr = "";

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public String getRewardStr() {
        return rewardStr;
    }

    public void setRewardStr(String rewardStr) {
        this.rewardStr = rewardStr;
    }

    public Integer getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(Integer loyalty) {
        this.loyalty = loyalty;
    }

    public String getLoyaltyStr() {
        return loyaltyStr;
    }

    public void setLoyaltyStr(String loyaltyStr) {
        this.loyaltyStr = loyaltyStr;
    }
}
