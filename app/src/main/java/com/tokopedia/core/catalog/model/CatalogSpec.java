package com.tokopedia.core.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogSpec implements Parcelable {

    @SerializedName("spec_header")
    @Expose
    private String specHeader;
    @SerializedName("spec_childs")
    @Expose
    private List<SpecChild> specChildList = new ArrayList<>();

    public List<SpecChild> getSpecChildList() {
        return specChildList;
    }

    public String getSpecHeader() {
        return specHeader;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.specHeader);
        dest.writeList(this.specChildList);
    }

    public CatalogSpec() {
    }

    protected CatalogSpec(Parcel in) {
        this.specHeader = in.readString();
        this.specChildList = new ArrayList<SpecChild>();
        in.readList(this.specChildList, SpecChild.class.getClassLoader());
    }

    public static final Parcelable.Creator<CatalogSpec> CREATOR = new Parcelable.Creator<CatalogSpec>() {
        @Override
        public CatalogSpec createFromParcel(Parcel source) {
            return new CatalogSpec(source);
        }

        @Override
        public CatalogSpec[] newArray(int size) {
            return new CatalogSpec[size];
        }
    };
}
