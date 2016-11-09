package com.tokopedia.discovery.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class SpecChild implements Parcelable {
    @SerializedName("spec_val")
    @Expose
    private List<String> specVal = new ArrayList<String>();
    @SerializedName("spec_key")
    @Expose
    private String specKey;

    public List<String> getSpecVal() {
        return specVal;
    }

    public String getSpecKey() {
        return specKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.specVal);
        dest.writeString(this.specKey);
    }

    public SpecChild() {
    }

    protected SpecChild(Parcel in) {
        this.specVal = in.createStringArrayList();
        this.specKey = in.readString();
    }

    public static final Parcelable.Creator<SpecChild> CREATOR = new Parcelable.Creator<SpecChild>() {
        @Override
        public SpecChild createFromParcel(Parcel source) {
            return new SpecChild(source);
        }

        @Override
        public SpecChild[] newArray(int size) {
            return new SpecChild[size];
        }
    };
}
