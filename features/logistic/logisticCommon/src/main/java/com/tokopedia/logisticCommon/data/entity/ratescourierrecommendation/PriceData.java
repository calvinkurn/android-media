package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class PriceData implements Parcelable {

    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("formatted_price")
    @Expose
    private String formattedPrice;

    public PriceData() {
    }

    protected PriceData(Parcel in) {
        price = in.readInt();
        formattedPrice = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(price);
        dest.writeString(formattedPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PriceData> CREATOR = new Creator<PriceData>() {
        @Override
        public PriceData createFromParcel(Parcel in) {
            return new PriceData(in);
        }

        @Override
        public PriceData[] newArray(int size) {
            return new PriceData[size];
        }
    };

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }
}
