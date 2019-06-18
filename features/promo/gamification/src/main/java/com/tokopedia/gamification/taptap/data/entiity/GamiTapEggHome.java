
package com.tokopedia.gamification.taptap.data.entiity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class GamiTapEggHome {

    @SerializedName("actionButton")
    private ArrayList<ActionButton> mActionButton;
    @SerializedName("backButton")
    private BackButton mBackButton;
    @SerializedName("rewardButton")
    private List<RewardButton> mRewardButton;
    @SerializedName("timeRemaining")
    private TimeRemaining mTimeRemaining;
    @SerializedName("tokenAsset")
    private TokenAsset mTokenAsset;
    @SerializedName("tokensUser")
    private TokensUser mTokensUser;

    public ArrayList<ActionButton> getActionButton() {
        return mActionButton;
    }

    public void setActionButton(ArrayList<ActionButton> actionButton) {
        mActionButton = actionButton;
    }

    public BackButton getBackButton() {
        return mBackButton;
    }

    public void setBackButton(BackButton backButton) {
        mBackButton = backButton;
    }

    public List<RewardButton> getRewardButton() {
        return mRewardButton;
    }

    public void setRewardButton(List<RewardButton> rewardButton) {
        mRewardButton = rewardButton;
    }

    public TimeRemaining getTimeRemaining() {
        return mTimeRemaining;
    }

    public void setTimeRemaining(TimeRemaining timeRemaining) {
        mTimeRemaining = timeRemaining;
    }

    public TokenAsset getTokenAsset() {
        return mTokenAsset;
    }

    public void setTokenAsset(TokenAsset tokenAsset) {
        mTokenAsset = tokenAsset;
    }

    public TokensUser getTokensUser() {
        return mTokensUser;
    }

    public void setTokensUser(TokensUser tokensUser) {
        mTokensUser = tokensUser;
    }

}
