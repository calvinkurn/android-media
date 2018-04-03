package com.tokopedia.gamification.floating.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenUser implements Parcelable {

    private Integer tokenUserID;
    private Integer campaignID;
    private String title;
    private Integer unixTimestampFetch;
    private Integer timeRemainingSeconds;
    private Boolean isShowTime;
    private String tokenClass;
    private TokenAsset tokenAsset;

    public Integer getTokenUserID() {
        return tokenUserID;
    }

    public Integer getCampaignID() {
        return campaignID;
    }

    public String getTitle() {
        return title;
    }

    public Integer getUnixTimestampFetch() {
        return unixTimestampFetch;
    }

    public Integer getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public Boolean getShowTime() {
        return isShowTime;
    }

    public String getTokenClass() {
        return tokenClass;
    }

    public TokenAsset getTokenAsset() {
        return tokenAsset;
    }

    public void setTokenUserID(Integer tokenUserID) {
        this.tokenUserID = tokenUserID;
    }

    public void setCampaignID(Integer campaignID) {
        this.campaignID = campaignID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnixTimestampFetch(Integer unixTimestampFetch) {
        this.unixTimestampFetch = unixTimestampFetch;
    }

    public void setTimeRemainingSeconds(Integer timeRemainingSeconds) {
        if (timeRemainingSeconds <= 0) {
            this.timeRemainingSeconds = 0;
        } else {
            this.timeRemainingSeconds = timeRemainingSeconds;
        }
    }

    public void setShowTime(Boolean showTime) {
        isShowTime = showTime;
    }

    public void setTokenClass(String tokenClass) {
        this.tokenClass = tokenClass;
    }

    public void setTokenAsset(TokenAsset tokenAsset) {
        this.tokenAsset = tokenAsset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.tokenUserID);
        dest.writeValue(this.campaignID);
        dest.writeString(this.title);
        dest.writeValue(this.unixTimestampFetch);
        dest.writeValue(this.timeRemainingSeconds);
        dest.writeValue(this.isShowTime);
        dest.writeString(this.tokenClass);
        dest.writeParcelable(this.tokenAsset, flags);
    }

    public TokenUser() {
    }

    protected TokenUser(Parcel in) {
        this.tokenUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.campaignID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.unixTimestampFetch = (Integer) in.readValue(Integer.class.getClassLoader());
        this.timeRemainingSeconds = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isShowTime = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.tokenClass = in.readString();
        this.tokenAsset = in.readParcelable(TokenAsset.class.getClassLoader());
    }

    public static final Parcelable.Creator<TokenUser> CREATOR = new Parcelable.Creator<TokenUser>() {
        @Override
        public TokenUser createFromParcel(Parcel source) {
            return new TokenUser(source);
        }

        @Override
        public TokenUser[] newArray(int size) {
            return new TokenUser[size];
        }
    };
}
