package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
@Deprecated
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
    @SerializedName("wholesale_min_number")
    @Expose
    private int wholesaleMinRaw;
    @SerializedName("wholesale_price_number")
    @Expose
    private int wholesalePriceRaw;
    @SerializedName("wholesale_max_number")
    @Expose
    private int wholesaleMaxRaw;

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

    public int getWholesaleMinRaw() {
        return wholesaleMinRaw;
    }

    public void setWholesaleMinRaw(int wholesaleMinRaw) {
        this.wholesaleMinRaw = wholesaleMinRaw;
    }

    public int getWholesalePriceRaw() {
        return wholesalePriceRaw;
    }

    public void setWholesalePriceRaw(int wholesalePriceRaw) {
        this.wholesalePriceRaw = wholesalePriceRaw;
    }

    public int getWholesaleMaxRaw() {
        return wholesaleMaxRaw;
    }

    public void setWholesaleMaxRaw(int wholesaleMaxRaw) {
        this.wholesaleMaxRaw = wholesaleMaxRaw;
    }

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

    public static class WholesaleComparator implements Comparator<ProductWholesalePrice> {

        @Override
        public int compare(ProductWholesalePrice o1, ProductWholesalePrice o2) {
            return o1.wholesalePrice.compareTo(o2.wholesalePrice);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.wholesaleMin);
        dest.writeString(this.wholesalePrice);
        dest.writeString(this.wholesaleMax);
        dest.writeInt(this.wholesaleMinRaw);
        dest.writeInt(this.wholesalePriceRaw);
        dest.writeInt(this.wholesaleMaxRaw);
    }

    protected ProductWholesalePrice(Parcel in) {
        this.wholesaleMin = in.readString();
        this.wholesalePrice = in.readString();
        this.wholesaleMax = in.readString();
        this.wholesaleMinRaw = in.readInt();
        this.wholesalePriceRaw = in.readInt();
        this.wholesaleMaxRaw = in.readInt();
    }

    public static final Creator<ProductWholesalePrice> CREATOR = new Creator<ProductWholesalePrice>() {
        @Override
        public ProductWholesalePrice createFromParcel(Parcel source) {
            return new ProductWholesalePrice(source);
        }

        @Override
        public ProductWholesalePrice[] newArray(int size) {
            return new ProductWholesalePrice[size];
        }
    };
}
