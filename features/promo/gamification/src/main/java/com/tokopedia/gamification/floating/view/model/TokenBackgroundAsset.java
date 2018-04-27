package com.tokopedia.gamification.floating.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 4/4/18.
 */

public class TokenBackgroundAsset implements Parcelable {

    private String name;
    private String version;
    private String backgroundImgUrl;

    public TokenBackgroundAsset() {
    }

    protected TokenBackgroundAsset(Parcel in) {
        name = in.readString();
        version = in.readString();
        backgroundImgUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(version);
        dest.writeString(backgroundImgUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokenBackgroundAsset> CREATOR = new Creator<TokenBackgroundAsset>() {
        @Override
        public TokenBackgroundAsset createFromParcel(Parcel in) {
            return new TokenBackgroundAsset(in);
        }

        @Override
        public TokenBackgroundAsset[] newArray(int size) {
            return new TokenBackgroundAsset[size];
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

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }
}
