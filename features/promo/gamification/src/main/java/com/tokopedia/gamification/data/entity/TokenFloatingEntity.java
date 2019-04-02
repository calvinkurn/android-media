package com.tokopedia.gamification.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenFloatingEntity implements Parcelable {

    @SerializedName("tokenId")
    @Expose
    private Integer tokenId;

    @SerializedName("tokenAsset")
    @Expose
    private TokenAssetEntity tokenAsset;

    @SerializedName("pageUrl")
    @Expose
    private String pageUrl;

    @SerializedName("applink")
    @Expose
    private String applink;

    @SerializedName("timeRemainingSeconds")
    @Expose
    private Integer timeRemainingSeconds;

    @SerializedName("isShowTime")
    @Expose
    private Boolean isShowTime;

    @SerializedName("unixTimestamp")
    @Expose
    private Integer unixTimestamp;

    protected TokenFloatingEntity(Parcel in) {
        if (in.readByte() == 0) {
            tokenId = null;
        } else {
            tokenId = in.readInt();
        }
        tokenAsset = in.readParcelable(TokenAssetEntity.class.getClassLoader());
        pageUrl = in.readString();
        applink = in.readString();
        if (in.readByte() == 0) {
            timeRemainingSeconds = null;
        } else {
            timeRemainingSeconds = in.readInt();
        }
        byte tmpIsShowTime = in.readByte();
        isShowTime = tmpIsShowTime == 0 ? null : tmpIsShowTime == 1;
        if (in.readByte() == 0) {
            unixTimestamp = null;
        } else {
            unixTimestamp = in.readInt();
        }
    }

    public static final Creator<TokenFloatingEntity> CREATOR = new Creator<TokenFloatingEntity>() {
        @Override
        public TokenFloatingEntity createFromParcel(Parcel in) {
            return new TokenFloatingEntity(in);
        }

        @Override
        public TokenFloatingEntity[] newArray(int size) {
            return new TokenFloatingEntity[size];
        }
    };

    public Integer getTokenId() {
        return tokenId;
    }

    public TokenAssetEntity getTokenAsset() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (tokenId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(tokenId);
        }
        dest.writeParcelable(tokenAsset, flags);
        dest.writeString(pageUrl);
        dest.writeString(applink);
        if (timeRemainingSeconds == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(timeRemainingSeconds);
        }
        dest.writeByte((byte) (isShowTime == null ? 0 : isShowTime ? 1 : 2));
        if (unixTimestamp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(unixTimestamp);
        }
    }
}
