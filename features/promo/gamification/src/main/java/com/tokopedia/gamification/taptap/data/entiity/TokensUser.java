
package com.tokopedia.gamification.taptap.data.entiity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.gamification.taptap.utils.TapTapConstants;


@SuppressWarnings("unused")
public class TokensUser {

    @SerializedName("campaignID")
    private long mCampaignID;
    @SerializedName("state")
    private String mState;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("tokenUserID")
    private long mTokenUserID;

    @Expose(serialize = false, deserialize = false)
    private boolean isEmptyState;

    public long getCampaignID() {
        return mCampaignID;
    }

    public void setCampaignID(long campaignID) {
        mCampaignID = campaignID;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getTokenUserID() {
        return mTokenUserID;
    }

    public void setTokenUserID(long tokenUserID) {
        mTokenUserID = tokenUserID;
    }

    public boolean isEmptyState() {
        return TapTapConstants.TokenState.STATE_EMPTY.equalsIgnoreCase(mState);
    }

}
