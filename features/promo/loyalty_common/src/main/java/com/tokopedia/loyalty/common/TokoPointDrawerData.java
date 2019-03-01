package com.tokopedia.loyalty.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 04/12/17.
 */

public class TokoPointDrawerData implements Parcelable {

    private int offFlag;
    private int hasNotif;
    private UserTier userTier;
    private PopUpNotif popUpNotif;
    private String mainPageUrl;
    private String mainPageTitle;
    private int sumCoupon;
    private String sumCouponStr;

    protected TokoPointDrawerData(Parcel in) {
        offFlag = in.readInt();
        hasNotif = in.readInt();
        userTier = in.readParcelable(UserTier.class.getClassLoader());
        popUpNotif = in.readParcelable(PopUpNotif.class.getClassLoader());
        sumCoupon = in.readInt();
        sumCouponStr = in.readString();
        mainPageUrl = in.readString();
        mainPageTitle = in.readString();
    }

    public static final Creator<TokoPointDrawerData> CREATOR = new Creator<TokoPointDrawerData>() {
        @Override
        public TokoPointDrawerData createFromParcel(Parcel in) {
            return new TokoPointDrawerData(in);
        }

        @Override
        public TokoPointDrawerData[] newArray(int size) {
            return new TokoPointDrawerData[size];
        }
    };

    public int getOffFlag() {
        return offFlag;
    }

    public void setOffFlag(int offFlag) {
        this.offFlag = offFlag;
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

    public PopUpNotif getPopUpNotif() {
        return popUpNotif;
    }

    public void setPopUpNotif(PopUpNotif popUpNotif) {
        this.popUpNotif = popUpNotif;
    }

    public String getMainPageUrl() {
        return mainPageUrl;
    }

    public String getMainPageTitle() {
        return mainPageTitle;

    }

    public void setMainPageTitle(String mainPageTitle) {
        this.mainPageTitle = mainPageTitle;
    }

    public void setMainPageUrl(String mainPageUrl) {
        this.mainPageUrl = mainPageUrl;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(offFlag);
        parcel.writeInt(hasNotif);
        parcel.writeParcelable(userTier, i);
        parcel.writeInt(sumCoupon);
        parcel.writeString(sumCouponStr);
        parcel.writeParcelable(popUpNotif, i);
        parcel.writeString(mainPageUrl);
        parcel.writeString(mainPageTitle);
    }

    public static class UserTier implements Parcelable {

        private String tierNameDesc;
        private String tierImageUrl;
        private String rewardPointsStr;

        protected UserTier(Parcel in) {
            tierNameDesc = in.readString();
            tierImageUrl = in.readString();
            rewardPointsStr = in.readString();
        }

        public static final Creator<UserTier> CREATOR = new Creator<UserTier>() {
            @Override
            public UserTier createFromParcel(Parcel in) {
                return new UserTier(in);
            }

            @Override
            public UserTier[] newArray(int size) {
                return new UserTier[size];
            }
        };

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
            return rewardPointsStr == null ? "" : rewardPointsStr;
        }

        public void setRewardPointsStr(String rewardPointsStr) {
            this.rewardPointsStr = rewardPointsStr;
        }

        public UserTier() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(tierNameDesc);
            parcel.writeString(tierImageUrl);
            parcel.writeString(rewardPointsStr);
        }
    }

    public TokoPointDrawerData() {
    }
}
