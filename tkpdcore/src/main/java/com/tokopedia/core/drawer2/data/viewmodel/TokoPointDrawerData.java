package com.tokopedia.core.drawer2.data.viewmodel;

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


    public static class PopUpNotif implements Parcelable {

        private String title;
        private String text;
        private String imageUrl;
        private String buttonText;
        private String buttonUrl;
        private String appLink;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getButtonText() {
            return buttonText;
        }

        public void setButtonText(String buttonText) {
            this.buttonText = buttonText;
        }

        public String getButtonUrl() {
            return buttonUrl;
        }

        public void setButtonUrl(String buttonUrl) {
            this.buttonUrl = buttonUrl;
        }

        public String getAppLink() {
            return appLink;
        }

        public void setAppLink(String appLink) {
            this.appLink = appLink;
        }

        public PopUpNotif() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.text);
            dest.writeString(this.imageUrl);
            dest.writeString(this.buttonText);
            dest.writeString(this.buttonUrl);
            dest.writeString(this.appLink);
        }

        protected PopUpNotif(Parcel in) {
            this.title = in.readString();
            this.text = in.readString();
            this.imageUrl = in.readString();
            this.buttonText = in.readString();
            this.buttonUrl = in.readString();
            this.appLink = in.readString();
        }

        public static final Creator<PopUpNotif> CREATOR = new Creator<PopUpNotif>() {
            @Override
            public PopUpNotif createFromParcel(Parcel source) {
                return new PopUpNotif(source);
            }

            @Override
            public PopUpNotif[] newArray(int size) {
                return new PopUpNotif[size];
            }
        };
    }

    public static class UserTier implements Parcelable {

        private String tierNameDesc;
        private String tierImageUrl;
        private String rewardPointsStr;

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
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.tierNameDesc);
            dest.writeString(this.tierImageUrl);
            dest.writeString(this.rewardPointsStr);
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

    public TokoPointDrawerData() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.offFlag);
        dest.writeInt(this.hasNotif);
        dest.writeParcelable(this.userTier, flags);
        dest.writeParcelable(this.popUpNotif, flags);
        dest.writeString(this.mainPageUrl);
        dest.writeString(this.mainPageTitle);
    }

    protected TokoPointDrawerData(Parcel in) {
        this.offFlag = in.readInt();
        this.hasNotif = in.readInt();
        this.userTier = in.readParcelable(UserTier.class.getClassLoader());
        this.popUpNotif = in.readParcelable(PopUpNotif.class.getClassLoader());
        this.mainPageUrl = in.readString();
        this.mainPageTitle = in.readString();
    }

    public static final Creator<TokoPointDrawerData> CREATOR = new Creator<TokoPointDrawerData>() {
        @Override
        public TokoPointDrawerData createFromParcel(Parcel source) {
            return new TokoPointDrawerData(source);
        }

        @Override
        public TokoPointDrawerData[] newArray(int size) {
            return new TokoPointDrawerData[size];
        }
    };
}
