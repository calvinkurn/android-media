package com.tokopedia.discovery.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogLocation implements Parcelable {

    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("total_shop")
    @Expose
    private String totalShop;

    public String getLocationName() {
        return locationName;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public String getTotalShop() {
        return totalShop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locationName);
        dest.writeValue(this.locationId);
        dest.writeString(this.totalShop);
    }

    public CatalogLocation() {
    }

    protected CatalogLocation(Parcel in) {
        this.locationName = in.readString();
        this.locationId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalShop = in.readString();
    }

    public static final Parcelable.Creator<CatalogLocation> CREATOR = new Parcelable.Creator<CatalogLocation>() {
        @Override
        public CatalogLocation createFromParcel(Parcel source) {
            return new CatalogLocation(source);
        }

        @Override
        public CatalogLocation[] newArray(int size) {
            return new CatalogLocation[size];
        }
    };
}
