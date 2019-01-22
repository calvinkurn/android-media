package com.tokopedia.home.beranda.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserTier implements Parcelable {
    private String tierNameDesc;
    private String tierImageUrl;
    private String rewardPointsStr;

    public UserTier(String tierNameDesc, String tierImageUrl, String rewardPointsStr) {
        this.tierNameDesc = tierNameDesc;
        this.tierImageUrl = tierImageUrl;
        this.rewardPointsStr = rewardPointsStr;
    }

    public String getTierNameDesc() {
        return tierNameDesc;
    }

    public void setTierNameDesc(String tierNameDesc) {
        this.tierNameDesc = tierNameDesc;
    }

    public String getTierImageUrl() {
        return tierImageUrl;
    }

    public void setTierImageUrl(String tierImageUrl) {
        this.tierImageUrl = tierImageUrl;
    }

    public String getRewardPointsStr() {
        return rewardPointsStr;
    }

    public void setRewardPointsStr(String rewardPointsStr) {
        this.rewardPointsStr = rewardPointsStr;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tierNameDesc);
        dest.writeString(this.tierImageUrl);
        dest.writeString(this.rewardPointsStr);
    }

    public UserTier() {
    }

    protected UserTier(Parcel in) {
        this.tierNameDesc = in.readString();
        this.tierImageUrl = in.readString();
        this.rewardPointsStr = in.readString();
    }

    public static final Creator<UserTier> CREATOR = new Creator<UserTier>() {
        @Override
        public UserTier createFromParcel(Parcel source) {
            return new UserTier(source);
        }

        @Override
        public UserTier[] newArray(int size) {
            return new UserTier[size];
        }
    };
}
