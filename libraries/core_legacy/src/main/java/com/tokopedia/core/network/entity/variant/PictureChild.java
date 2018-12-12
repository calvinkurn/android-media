
package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PictureChild implements Parcelable {

    @SerializedName("original")
    @Expose
    private String original;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    protected PictureChild(Parcel in) {
        original = in.readString();
        thumbnail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original);
        dest.writeString(thumbnail);
    }

    @SuppressWarnings("unused")
    public static final Creator<PictureChild> CREATOR = new Creator<PictureChild>() {
        @Override
        public PictureChild createFromParcel(Parcel in) {
            return new PictureChild(in);
        }

        @Override
        public PictureChild[] newArray(int size) {
            return new PictureChild[size];
        }
    };
}