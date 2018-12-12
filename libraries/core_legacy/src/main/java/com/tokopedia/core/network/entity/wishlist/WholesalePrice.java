package com.tokopedia.core.network.entity.wishlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ricoharisin on 4/15/16.
 */
public class WholesalePrice implements Parcelable {

    @SerializedName("minimum")
    int Minimum;
    @SerializedName("maximum")
    int Maximum;
    @SerializedName("price")
    int Price;

    public int getMinimum() {
        return Minimum;
    }

    public void setMinimum(int minimum) {
        Minimum = minimum;
    }

    public int getMaximum() {
        return Maximum;
    }

    public void setMaximum(int maximum) {
        Maximum = maximum;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Minimum);
        dest.writeInt(this.Maximum);
        dest.writeInt(this.Price);
    }

    public WholesalePrice() {
    }

    protected WholesalePrice(Parcel in) {
        this.Minimum = in.readInt();
        this.Maximum = in.readInt();
        this.Price = in.readInt();
    }

    public static final Creator<WholesalePrice> CREATOR = new Creator<WholesalePrice>() {
        @Override
        public WholesalePrice createFromParcel(Parcel source) {
            return new WholesalePrice(source);
        }

        @Override
        public WholesalePrice[] newArray(int size) {
            return new WholesalePrice[size];
        }
    };
}
