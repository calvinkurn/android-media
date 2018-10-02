package com.tokopedia.wishlist.common.data.source.cloud.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class Pagination implements Parcelable {

    @SerializedName("next_url")
    String NextUrl;

    public String getNextUrl() {
        return NextUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.NextUrl);
    }

    public Pagination() {
    }

    protected Pagination(Parcel in) {
        this.NextUrl = in.readString();
    }

    public static final Parcelable.Creator<Pagination> CREATOR = new Parcelable.Creator<Pagination>() {
        @Override
        public Pagination createFromParcel(Parcel source) {
            return new Pagination(source);
        }

        @Override
        public Pagination[] newArray(int size) {
            return new Pagination[size];
        }
    };

}
