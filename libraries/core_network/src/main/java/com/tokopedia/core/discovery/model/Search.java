package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author kulomady on 12/22/16.
 */

@Deprecated
public class Search implements Parcelable {

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
