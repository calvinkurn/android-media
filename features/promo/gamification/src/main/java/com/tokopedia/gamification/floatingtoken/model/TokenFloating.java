package com.tokopedia.gamification.floatingtoken.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenFloating implements Parcelable{

    private String tokenClass;
    private Integer tokenId;
    private TokenAsset tokenAsset;
    private String pageUrl;
    private String applink;
    private Integer timeRemainingSeconds;
    private Boolean isShowTime;
    private Integer unixTimestamp;

    public String getTokenClass() {
        return tokenClass;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public TokenAsset getTokenAsset() {
        return tokenAsset;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getApplink() {
        return applink;
    }

    public Integer getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public Boolean getShowTime() {
        return isShowTime;
    }

    public Integer getUnixTimestamp() {
        return unixTimestamp;
    }

    public void setTokenClass(String tokenClass) {
        this.tokenClass = tokenClass;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

    public void setTokenAsset(TokenAsset tokenAsset) {
        this.tokenAsset = tokenAsset;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public void setTimeRemainingSeconds(Integer timeRemainingSeconds) {
        this.timeRemainingSeconds = timeRemainingSeconds;
    }

    public void setShowTime(Boolean showTime) {
        isShowTime = showTime;
    }

    public void setUnixTimestamp(Integer unixTimestamp) {
        this.unixTimestamp = unixTimestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenClass);
        dest.writeValue(this.tokenId);
        dest.writeParcelable(this.tokenAsset, flags);
        dest.writeString(this.pageUrl);
        dest.writeString(this.applink);
        dest.writeValue(this.timeRemainingSeconds);
        dest.writeValue(this.isShowTime);
        dest.writeValue(this.unixTimestamp);
    }

    public TokenFloating() {
    }

    protected TokenFloating(Parcel in) {
        this.tokenClass = in.readString();
        this.tokenId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tokenAsset = in.readParcelable(TokenAsset.class.getClassLoader());
        this.pageUrl = in.readString();
        this.applink = in.readString();
        this.timeRemainingSeconds = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isShowTime = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.unixTimestamp = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<TokenFloating> CREATOR = new Creator<TokenFloating>() {
        @Override
        public TokenFloating createFromParcel(Parcel source) {
            return new TokenFloating(source);
        }

        @Override
        public TokenFloating[] newArray(int size) {
            return new TokenFloating[size];
        }
    };
}
