package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class RangePriceData implements Parcelable {

    @SerializedName("min_price")
    @Expose
    private double minPrice;
    @SerializedName("max_price")
    @Expose
    private double maxPrice;

    public RangePriceData() {
    }

    protected RangePriceData(Parcel in) {
        minPrice = in.readInt();
        maxPrice = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(minPrice);
        dest.writeDouble(maxPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RangePriceData> CREATOR = new Creator<RangePriceData>() {
        @Override
        public RangePriceData createFromParcel(Parcel in) {
            return new RangePriceData(in);
        }

        @Override
        public RangePriceData[] newArray(int size) {
            return new RangePriceData[size];
        }
    };

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }
}
