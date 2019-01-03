package com.tokopedia.core.network.entity.wishlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ricoharisin on 4/15/16.
 */
public class Pagination implements Parcelable {

    @SerializedName("next_url")
    String NextUrl;

    public void setNextUrl(String nextUrl) {
        NextUrl = nextUrl;
    }

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

    public static final Creator<Pagination> CREATOR = new Creator<Pagination>() {
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
