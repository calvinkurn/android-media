package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoPointStatusPointsEntity {
    @Expose
    @SerializedName("loyalty")
    private int loyalty;

    @Expose
    @SerializedName("reward")
    private int reward;

    @Expose
    @SerializedName("loyaltyExpiryInfo")
    private String loyaltyExpiryInfo;

    @Expose
    @SerializedName("rewardExpiryInfo")
    private String rewardExpiryInfo;

    @Expose
    @SerializedName("loyaltyStr")
    private String loyaltyStr;

    @Expose
    @SerializedName("rewardStr")
    private String rewardStr;

    @SerializedName("eggImageHomepageURL")
    private String eggImageHomepageURL;

    @SerializedName("backgroundImgURL")
    private String backgroundImgURL;

    @SerializedName("backgroundImgURLMobile")
    private String backgroundImgURLMobile;

    public String getEggImageHomepageURL() {
        return eggImageHomepageURL;
    }

    public void setEggImageHomepageURL(String eggImageHomepageURL) {
        this.eggImageHomepageURL = eggImageHomepageURL;
    }

    public String getBackgroundImgURL() {
        return backgroundImgURL;
    }

    public void setBackgroundImgURL(String backgroundImgURL) {
        this.backgroundImgURL = backgroundImgURL;
    }

    public String getBackgroundImgURLMobile() {
        return backgroundImgURLMobile;
    }

    public void setBackgroundImgURLMobile(String backgroundImgURLMobile) {
        this.backgroundImgURLMobile = backgroundImgURLMobile;
    }

    public int getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(int loyalty) {
        this.loyalty = loyalty;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getLoyaltyExpiryInfo() {
        return loyaltyExpiryInfo;
    }

    public void setLoyaltyExpiryInfo(String loyaltyExpiryInfo) {
        this.loyaltyExpiryInfo = loyaltyExpiryInfo;
    }

    public String getRewardExpiryInfo() {
        return rewardExpiryInfo;
    }

    public void setRewardExpiryInfo(String rewardExpiryInfo) {
        this.rewardExpiryInfo = rewardExpiryInfo;
    }

    public String getLoyaltyStr() {
        return loyaltyStr;
    }

    public void setLoyaltyStr(String loyaltyStr) {
        this.loyaltyStr = loyaltyStr;
    }

    public String getRewardStr() {
        return rewardStr;
    }

    public void setRewardStr(String rewardStr) {
        this.rewardStr = rewardStr;
    }

    @Override
    public String toString() {
        return "TokoPointStatusPointsEntity{" +
                "loyalty=" + loyalty +
                ", reward=" + reward +
                ", loyaltyExpiryInfo='" + loyaltyExpiryInfo + '\'' +
                ", rewardExpiryInfo='" + rewardExpiryInfo + '\'' +
                ", loyaltyStr='" + loyaltyStr + '\'' +
                ", rewardStr='" + rewardStr + '\'' +
                '}';
    }
}
