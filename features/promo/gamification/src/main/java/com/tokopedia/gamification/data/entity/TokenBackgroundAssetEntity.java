package com.tokopedia.gamification.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 4/4/18.
 */

public class TokenBackgroundAssetEntity implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("backgroundImgUrl")
    @Expose
    private String backgroundImgUrl;

    protected TokenBackgroundAssetEntity(Parcel in) {
        name = in.readString();
        version = in.readString();
        backgroundImgUrl = in.readString();
    }

    public static final Creator<TokenBackgroundAssetEntity> CREATOR = new Creator<TokenBackgroundAssetEntity>() {
        @Override
        public TokenBackgroundAssetEntity createFromParcel(Parcel in) {
            return new TokenBackgroundAssetEntity(in);
        }

        @Override
        public TokenBackgroundAssetEntity[] newArray(int size) {
            return new TokenBackgroundAssetEntity[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(version);
        dest.writeString(backgroundImgUrl);
    }
}
