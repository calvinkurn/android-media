package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
public class ProductWholesalePrice implements Parcelable {
    private static final String TAG = ProductWholesalePrice.class.getSimpleName();

    @SerializedName("wholesale_min")
    @Expose
    private String wholesaleMin;
    @SerializedName("wholesale_price")
    @Expose
    private String wholesalePrice;
    @SerializedName("wholesale_max")
    @Expose
    private String wholesaleMax;

    public ProductWholesalePrice() {
    }

    public String getWholesaleMin() {
        return wholesaleMin;
    }

    public void setWholesaleMin(String wholesaleMin) {
        this.wholesaleMin = wholesaleMin;
    }

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public String getWholesaleMax() {
        return wholesaleMax;
    }

    public void setWholesaleMax(String wholesaleMax) {
        this.wholesaleMax = wholesaleMax;
    }

    protected ProductWholesalePrice(Parcel in) {
        wholesaleMin = in.readString();
        wholesalePrice = in.readString();
        wholesaleMax = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wholesaleMin);
        dest.writeString(wholesalePrice);
        dest.writeString(wholesaleMax);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductWholesalePrice> CREATOR = new Parcelable.Creator<ProductWholesalePrice>() {
        @Override
        public ProductWholesalePrice createFromParcel(Parcel in) {
            return new ProductWholesalePrice(in);
        }

        @Override
        public ProductWholesalePrice[] newArray(int size) {
            return new ProductWholesalePrice[size];
        }
    };


    public static class Builder {
        private String wholesaleMin;
        private String wholesalePrice;
        private String wholesaleMax;

        private Builder() {
        }

        public static Builder aProductWholesalePrice() {
            return new Builder();
        }

        public Builder setWholesaleMin(String wholesaleMin) {
            this.wholesaleMin = wholesaleMin;
            return this;
        }

        public Builder setWholesalePrice(String wholesalePrice) {
            this.wholesalePrice = wholesalePrice;
            return this;
        }

        public Builder setWholesaleMax(String wholesaleMax) {
            this.wholesaleMax = wholesaleMax;
            return this;
        }

        public Builder but() {
            return aProductWholesalePrice().setWholesaleMin(wholesaleMin).setWholesalePrice(wholesalePrice).setWholesaleMax(wholesaleMax);
        }

        public ProductWholesalePrice build() {
            ProductWholesalePrice productWholesalePrice = new ProductWholesalePrice();
            productWholesalePrice.setWholesaleMin(wholesaleMin);
            productWholesalePrice.setWholesalePrice(wholesalePrice);
            productWholesalePrice.setWholesaleMax(wholesaleMax);
            return productWholesalePrice;
        }
    }
}
