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

    public static class Catalog implements Parcelable {

        private String title;
        private String subTitle;
        private int points;
        private String thumbnailUrl;
        private String thumbnailUrlMobile;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getThumbnailUrlMobile() {
            return thumbnailUrlMobile;
        }

        public void setThumbnailUrlMobile(String thumbnailUrlMobile) {
            this.thumbnailUrlMobile = thumbnailUrlMobile;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.subTitle);
            dest.writeInt(this.points);
            dest.writeString(this.thumbnailUrl);
            dest.writeString(this.thumbnailUrlMobile);
        }

        public Catalog() {
        }

        protected Catalog(Parcel in) {
            this.title = in.readString();
            this.subTitle = in.readString();
            this.points = in.readInt();
            this.thumbnailUrl = in.readString();
            this.thumbnailUrlMobile = in.readString();
        }

        public static final Parcelable.Creator<Catalog> CREATOR = new Parcelable.Creator<Catalog>() {
            @Override
            public Catalog createFromParcel(Parcel source) {
                return new Catalog(source);
            }

            @Override
            public Catalog[] newArray(int size) {
                return new Catalog[size];
            }
        };
    }

    public static class PopUpNotif implements Parcelable {

        private String title;
        private String text;
        private String imageUrl;
        private String buttonText;
        private String buttonUrl;
        private String appLink;
        private String notes;
        private Catalog catalog;

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

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Catalog getCatalog() {
            return catalog;
        }

        public void setCatalog(Catalog catalog) {
            this.catalog = catalog;
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
            dest.writeString(this.notes);
            dest.writeParcelable(this.catalog, flags);
        }

        protected PopUpNotif(Parcel in) {
            this.title = in.readString();
            this.text = in.readString();
            this.imageUrl = in.readString();
            this.buttonText = in.readString();
            this.buttonUrl = in.readString();
            this.appLink = in.readString();
            this.notes = in.readString();
            this.catalog = in.readParcelable(Catalog.class.getClassLoader());
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

        private int tierId;
        private String tierName;
        private String tierNameDesc;
        private String tierImageUrl;
        private int rewardPoints;
        private String rewardPointsStr;
        private String mainPageUrl;

        public int getTierId() {
            return tierId;
        }

        public void setTierId(int tierId) {
            this.tierId = tierId;
        }

        public String getTierName() {
            return tierName;
        }

        public String getTierNameDesc() {
            return tierNameDesc;
        }

        public void setTierNameDesc(String tierNameDesc) {
            this.tierNameDesc = tierNameDesc;
        }

        public void setTierName(String tierName) {
            this.tierName = tierName;
        }

        public String getTierImageUrl() {
            return tierImageUrl;
        }

        public void setTierImageUrl(String tierImageUrl) {
            this.tierImageUrl = tierImageUrl;
        }

        public int getRewardPoints() {
            return rewardPoints;
        }

        public void setRewardPoints(int rewardPoints) {
            this.rewardPoints = rewardPoints;
        }

        public String getRewardPointsStr() {
            return rewardPointsStr == null ? "" : rewardPointsStr;
        }

        public void setRewardPointsStr(String rewardPointsStr) {
            this.rewardPointsStr = rewardPointsStr;
        }

        public UserTier() {
        }

        public String getMainPageUrl() {
            return mainPageUrl;
        }

        public void setMainPageUrl(String mainPageUrl) {
            this.mainPageUrl = mainPageUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.tierId);
            dest.writeString(this.tierName);
            dest.writeString(this.tierNameDesc);
            dest.writeString(this.tierImageUrl);
            dest.writeInt(this.rewardPoints);
            dest.writeString(this.rewardPointsStr);
            dest.writeString(this.mainPageUrl);
        }

        protected UserTier(Parcel in) {
            this.tierId = in.readInt();
            this.tierName = in.readString();
            this.tierNameDesc = in.readString();
            this.tierImageUrl = in.readString();
            this.rewardPoints = in.readInt();
            this.rewardPointsStr = in.readString();
            this.mainPageUrl = in.readString();
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
