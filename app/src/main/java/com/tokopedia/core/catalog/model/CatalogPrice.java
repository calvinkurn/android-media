package com.tokopedia.core.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogPrice implements Parcelable {
    @SerializedName("price_max")
    @Expose
    private String priceMax;
    @SerializedName("price_min")
    @Expose
    private String priceMin;

    public String getPriceMax() {
        return priceMax;
    }

    public String getPriceMin() {
        return priceMin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.priceMax);
        dest.writeString(this.priceMin);
    }

    public CatalogPrice() {
    }

    protected CatalogPrice(Parcel in) {
        this.priceMax = in.readString();
        this.priceMin = in.readString();
    }

    public static final Parcelable.Creator<CatalogPrice> CREATOR = new Parcelable.Creator<CatalogPrice>() {
        @Override
        public CatalogPrice createFromParcel(Parcel source) {
            return new CatalogPrice(source);
        }

        @Override
        public CatalogPrice[] newArray(int size) {
            return new CatalogPrice[size];
        }
    };
}
