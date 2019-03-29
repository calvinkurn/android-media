package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 5/16/18.
 */

public class GqlTokoPointPoints {

    @SerializedName("reward")
    @Expose
    private long reward;

    @SerializedName("rewardStr")
    @Expose
    private String rewardString;

    public long getReward() {
        return reward;
    }

    public void setReward(long reward) {
        this.reward = reward;
    }

    public String getRewardString() {
        return rewardString;
    }

    public void setRewardString(String rewardString) {
        this.rewardString = rewardString;
    }
}
