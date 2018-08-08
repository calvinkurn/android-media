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
    private TokenAsset tokenAsset;
    private TokenBackgroundAsset backgroundAsset;

    public TokenUser() {
    }

    protected TokenUser(Parcel in) {
        if (in.readByte() == 0) {
            tokenUserID = null;
        } else {
            tokenUserID = in.readInt();
        }
        if (in.readByte() == 0) {
            campaignID = null;
        } else {
            campaignID = in.readInt();
        }
        title = in.readString();
        if (in.readByte() == 0) {
            unixTimestampFetch = null;
        } else {
            unixTimestampFetch = in.readInt();
        }
        if (in.readByte() == 0) {
            timeRemainingSeconds = null;
        } else {
            timeRemainingSeconds = in.readInt();
        }
        byte tmpIsShowTime = in.readByte();
        isShowTime = tmpIsShowTime == 0 ? null : tmpIsShowTime == 1;
        tokenAsset = in.readParcelable(TokenAsset.class.getClassLoader());
        backgroundAsset = in.readParcelable(TokenBackgroundAsset.class.getClassLoader());
    }

    public static final Creator<TokenUser> CREATOR = new Creator<TokenUser>() {
        @Override
        public TokenUser createFromParcel(Parcel in) {
            return new TokenUser(in);
        }

        @Override
        public TokenUser[] newArray(int size) {
            return new TokenUser[size];
        }
    };

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

    public void setTokenAsset(TokenAsset tokenAsset) {
        this.tokenAsset = tokenAsset;
    }

    public TokenBackgroundAsset getBackgroundAsset() {
        return backgroundAsset;
    }

    public void setBackgroundAsset(TokenBackgroundAsset backgroundAsset) {
        this.backgroundAsset = backgroundAsset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (tokenUserID == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(tokenUserID);
        }
        if (campaignID == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(campaignID);
        }
        parcel.writeString(title);
        if (unixTimestampFetch == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(unixTimestampFetch);
        }
        if (timeRemainingSeconds == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(timeRemainingSeconds);
        }
        parcel.writeByte((byte) (isShowTime == null ? 0 : isShowTime ? 1 : 2));
        parcel.writeParcelable(tokenAsset, i);
        parcel.writeParcelable(backgroundAsset, i);
    }
}
