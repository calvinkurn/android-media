
package com.tokopedia.core.network.entity.home.recentView;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Badge implements Parcelable {

    @SerializedName("image_url")
    private String mImageUrl;
    @SerializedName("title")
    private String mTitle;

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String image_url) {
        mImageUrl = image_url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageUrl);
        dest.writeString(this.mTitle);
    }

    public Badge() {
    }

    protected Badge(Parcel in) {
        this.mImageUrl = in.readString();
        this.mTitle = in.readString();
    }

    public static final Parcelable.Creator<Badge> CREATOR = new Parcelable.Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel source) {
            return new Badge(source);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };
}
