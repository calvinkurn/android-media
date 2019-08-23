package com.tokopedia.discovery.common.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Search implements Parcelable {

    @SerializedName("searchable")
    @Expose
    int searchable;
    @SerializedName("placeholder")
    @Expose
    String placeholder;

    public Search() {
    }

    public int getSearchable() {
        return searchable;
    }

    public void setSearchable(int searchable) {
        this.searchable = searchable;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.searchable);
        dest.writeString(this.placeholder);
    }

    protected Search(Parcel in) {
        this.searchable = in.readInt();
        this.placeholder = in.readString();
    }

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        @Override
        public Search createFromParcel(Parcel source) {
            return new Search(source);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[size];
        }
    };
}
