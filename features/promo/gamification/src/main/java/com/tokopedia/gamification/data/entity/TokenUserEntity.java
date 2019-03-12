package com.tokopedia.gamification.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenUserEntity implements Parcelable {

    @SerializedName("tokenUserID")
    @Expose
    private Integer tokenUserID;

    @SerializedName("campaignID")
    @Expose
    private Integer campaignID;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("unixTimestampFetch")
    @Expose
    private Integer unixTimestampFetch;

    @SerializedName("timeRemainingSeconds")
    @Expose
    private Integer timeRemainingSeconds;

    @SerializedName("isShowTime")
    @Expose
    private Boolean isShowTime;

    @SerializedName("backgroundAsset")
    @Expose
    private TokenBackgroundAssetEntity backgroundAsset;

    @SerializedName("tokenAsset")
    @Expose
    private TokenAssetEntity tokenAsset;

    protected TokenUserEntity(Parcel in) {
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
        backgroundAsset = in.readParcelable(TokenBackgroundAssetEntity.class.getClassLoader());
        tokenAsset = in.readParcelable(TokenAssetEntity.class.getClassLoader());
    }

    public static final Creator<TokenUserEntity> CREATOR = new Creator<TokenUserEntity>() {
        @Override
        public TokenUserEntity createFromParcel(Parcel in) {
            return new TokenUserEntity(in);
        }

        @Override
        public TokenUserEntity[] newArray(int size) {
            return new TokenUserEntity[size];
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

    public void setTimeRemainingSeconds(Integer timeRemainingSeconds) {
        this.timeRemainingSeconds = timeRemainingSeconds;
    }

    public Boolean getShowTime() {
        return isShowTime;
    }

    public TokenAssetEntity getTokenAsset() {
        return tokenAsset;
    }

    public TokenBackgroundAssetEntity getBackgroundAsset() {
        return backgroundAsset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (tokenUserID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(tokenUserID);
        }
        if (campaignID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(campaignID);
        }
        dest.writeString(title);
        if (unixTimestampFetch == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(unixTimestampFetch);
        }
        if (timeRemainingSeconds == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(timeRemainingSeconds);
        }
        dest.writeByte((byte) (isShowTime == null ? 0 : isShowTime ? 1 : 2));
        dest.writeParcelable(backgroundAsset, flags);
        dest.writeParcelable(tokenAsset, flags);
    }
}
