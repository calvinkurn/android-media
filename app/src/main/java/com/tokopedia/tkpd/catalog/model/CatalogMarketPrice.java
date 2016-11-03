package com.tokopedia.tkpd.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogMarketPrice implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("min_price")
    @Expose
    private String minPrice;
    @SerializedName("max_price")
    @Expose
    private String maxPrice;

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.time);
        dest.writeString(this.minPrice);
        dest.writeString(this.maxPrice);
    }

    public CatalogMarketPrice() {
    }

    protected CatalogMarketPrice(Parcel in) {
        this.name = in.readString();
        this.time = in.readString();
        this.minPrice = in.readString();
        this.maxPrice = in.readString();
    }

    public static final Parcelable.Creator<CatalogMarketPrice> CREATOR = new Parcelable.Creator<CatalogMarketPrice>() {
        @Override
        public CatalogMarketPrice createFromParcel(Parcel source) {
            return new CatalogMarketPrice(source);
        }

        @Override
        public CatalogMarketPrice[] newArray(int size) {
            return new CatalogMarketPrice[size];
        }
    };
}
