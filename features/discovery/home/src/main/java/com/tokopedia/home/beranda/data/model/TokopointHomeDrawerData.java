package com.tokopedia.home.beranda.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TokopointHomeDrawerData implements Parcelable {
    private int offFlag;
    private int hasNotif;
    private UserTier userTier;
    private String rewardPointsStr;
    private String mainPageUrl;
    private String mainPageTitle;
    private int sumCoupon;
    private String sumCouponStr;

    public TokopointHomeDrawerData(int offFlag,
                                   int hasNotif,
                                   UserTier userTier,
                                   String rewardPointsStr,
                                   String mainPageUrl,
                                   String mainPageTitle,
                                   int sumCoupon,
                                   String sumCouponStr) {
        this.offFlag = offFlag;
        this.hasNotif = hasNotif;
        this.userTier = userTier;
        this.rewardPointsStr = rewardPointsStr;
        this.mainPageUrl = mainPageUrl;
        this.mainPageTitle = mainPageTitle;
        this.sumCoupon = sumCoupon;
        this.sumCouponStr = sumCouponStr;
    }

    public int getOffFlag() {
        return offFlag;
    }

    public void setOffFlag(int offFlag) {
        this.offFlag = offFlag;
    }

    public String getRewardPointsStr() {
        return rewardPointsStr;
    }

    public void setRewardPointsStr(String rewardPointsStr) {
        this.rewardPointsStr = rewardPointsStr;
    }

    public int getHasNotif() {
        return hasNotif;
    }

    public void setHasNotif(int hasNotif) {
        this.hasNotif = hasNotif;
    }

    public UserTier getUserTier() {
        return userTier;
    }

    public void setUserTier(UserTier userTier) {
        this.userTier = userTier;
    }

    public int getSumCoupon() {
        return sumCoupon;
    }

    public void setSumCoupon(int sumCoupon) {
        this.sumCoupon = sumCoupon;
    }

    public String getSumCouponStr() {
        return sumCouponStr;
    }

    public void setSumCouponStr(String sumCouponStr) {
        this.sumCouponStr = sumCouponStr;
    }

    public String getMainPageUrl() {
        return mainPageUrl;
    }

    public void setMainPageUrl(String mainPageUrl) {
        this.mainPageUrl = mainPageUrl;
    }

    public String getMainPageTitle() {
        return mainPageTitle;
    }

    public void setMainPageTitle(String mainPageTitle) {
        this.mainPageTitle = mainPageTitle;
    }

    public static Creator<TokopointHomeDrawerData> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.offFlag);
        dest.writeInt(this.hasNotif);
        dest.writeString(this.rewardPointsStr);
        dest.writeString(this.mainPageUrl);
        dest.writeString(this.mainPageTitle);
        dest.writeInt(this.sumCoupon);
        dest.writeString(this.sumCouponStr);
    }

    protected TokopointHomeDrawerData(Parcel in) {
        this.offFlag = in.readInt();
        this.hasNotif = in.readInt();
        this.rewardPointsStr = in.readString();
        this.mainPageUrl = in.readString();
        this.mainPageTitle = in.readString();
        this.sumCoupon = in.readInt();
        this.sumCouponStr = in.readString();
    }

    public static final Creator<TokopointHomeDrawerData> CREATOR = new Creator<TokopointHomeDrawerData>() {
        @Override
        public TokopointHomeDrawerData createFromParcel(Parcel source) {
            return new TokopointHomeDrawerData(source);
        }

        @Override
        public TokopointHomeDrawerData[] newArray(int size) {
            return new TokopointHomeDrawerData[size];
        }
    };
}
