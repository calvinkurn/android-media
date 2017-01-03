package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author kulomady on 12/22/16.
 */
public class Search implements Serializable, Parcelable {

    @SerializedName("searchable")
    @Expose
    int searchable;
    @SerializedName("placeholder")
    @Expose
    String placeholder;

    public Search() {
    }

    /**
     * @return The searchable
     */
    public int getSearchable() {
        return searchable;
    }

    /**
     * @param searchable The searchable
     */
    public void setSearchable(int searchable) {
        this.searchable = searchable;
    }

    /**
     * @return The placeholder
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * @param placeholder The placeholder
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }


    protected Search(Parcel in) {
        searchable = in.readInt();
        placeholder = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(searchable);
        dest.writeString(placeholder);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Search> CREATOR = new Parcelable.Creator<Search>() {
        @Override
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[size];
        }
    };
}
