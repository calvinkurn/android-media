package com.tokopedia.core.drawer2.data.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 04/12/17.
 */

public class TopPointDrawerData {

    @SerializedName("off_flag")
    @Expose
    private int offFlag;
    @SerializedName("has_notif")
    @Expose
    private int hasNotif;
    @SerializedName("user_tier")
    @Expose
    private UserTier userTier;
    @SerializedName("pop_up_notif")
    @Expose
    private PopUpNotif popUpNotif;

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

    public static class Catalog {
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("sub_title")
        @Expose
        private String subTitle;
        @SerializedName("points")
        @Expose
        private int points;
        @SerializedName("thumbnail_url")
        @Expose
        private String thumbnailUrl;
        @SerializedName("thumbnail_url_mobile")
        @Expose
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
    }

    public static class PopUpNotif {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("button_text")
        @Expose
        private String buttonText;
        @SerializedName("button_url")
        @Expose
        private String buttonUrl;
        @SerializedName("app_link")
        @Expose
        private String appLink;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("catalog")
        @Expose
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
    }

    public static class UserTier {

        @SerializedName("tier_id")
        @Expose
        private int tierId;
        @SerializedName("tier_name")
        @Expose
        private String tierName;
        @SerializedName("tier_image_url")
        @Expose
        private String tierImageUrl;
        @SerializedName("reward_points")
        @Expose
        private int rewardPoints;

        public int getTierId() {
            return tierId;
        }

        public void setTierId(int tierId) {
            this.tierId = tierId;
        }

        public String getTierName() {
            return tierName;
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
    }
}
