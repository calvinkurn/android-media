package com.tokopedia.core.network.entity.intermediary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alifa on 3/8/2017.
 */

@Deprecated
public class SimpleCategory implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public SimpleCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected SimpleCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SimpleCategory> CREATOR = new Parcelable.Creator<SimpleCategory>() {
        @Override
        public SimpleCategory createFromParcel(Parcel in) {
            return new SimpleCategory(in);
        }

        @Override
        public SimpleCategory[] newArray(int size) {
            return new SimpleCategory[size];
        }
    };
}