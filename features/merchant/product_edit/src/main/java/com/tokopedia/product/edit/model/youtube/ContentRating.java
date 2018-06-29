package com.tokopedia.product.edit.model.youtube;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentRating implements Parcelable {

    @SerializedName("ytRating")
    @Expose
    private String ytRating;

    public String getYtRating() {
        return ytRating;
    }

    public void setYtRating(String ytRating) {
        this.ytRating = ytRating;
    }


    protected ContentRating(Parcel in) {
        ytRating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ytRating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ContentRating> CREATOR = new Parcelable.Creator<ContentRating>() {
        @Override
        public ContentRating createFromParcel(Parcel in) {
            return new ContentRating(in);
        }

        @Override
        public ContentRating[] newArray(int size) {
            return new ContentRating[size];
        }
    };
}
