package com.tokopedia.home.beranda.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TokopointHomeDrawerData implements Parcelable {
    private int offFlag;
    private String rewardPointsStr;
    private String mainPageUrl;
    private String mainPageTitle;

    public TokopointHomeDrawerData(int offFlag, String rewardPointsStr, String mainPageUrl, String mainPageTitle) {
        this.offFlag = offFlag;
        this.rewardPointsStr = rewardPointsStr;
        this.mainPageUrl = mainPageUrl;
        this.mainPageTitle = mainPageTitle;
    }

    protected TokopointHomeDrawerData(Parcel in) {
        offFlag = in.readInt();
        rewardPointsStr = in.readString();
        mainPageUrl = in.readString();
        mainPageTitle = in.readString();
    }

    public static final Creator<TokopointHomeDrawerData> CREATOR = new Creator<TokopointHomeDrawerData>() {
        @Override
        public TokopointHomeDrawerData createFromParcel(Parcel in) {
            return new TokopointHomeDrawerData(in);
        }

        @Override
        public TokopointHomeDrawerData[] newArray(int size) {
            return new TokopointHomeDrawerData[size];
        }
    };

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
    public void writeToParcel(Parcel parcel, int i) {
        offFlag = parcel.readInt();
        rewardPointsStr = parcel.readString();
        mainPageUrl = parcel.readString();
        mainPageTitle = parcel.readString();
    }
}
