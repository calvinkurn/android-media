package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 10/25/16. Tokopedia
 */
@Deprecated
public class ProductCashback implements Parcelable{
    @SerializedName("product_cashback")
    @Expose
    private String productCashback;
    @SerializedName("product_cashback_value")
    @Expose
    private String productCashbackValue;

    protected ProductCashback(Parcel in) {
        productCashback = in.readString();
        productCashbackValue = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productCashback);
        dest.writeString(productCashbackValue);
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductCashback> CREATOR = new Creator<ProductCashback>() {
        @Override
        public ProductCashback createFromParcel(Parcel in) {
            return new ProductCashback(in);
        }

        @Override
        public ProductCashback[] newArray(int size) {
            return new ProductCashback[size];
        }
    };

    public String getProductCashback() {
        return productCashback;
    }

    public void setProductCashback(String productCashback) {
        this.productCashback = productCashback;
    }

    public String getProductCashbackValue() {
        return productCashbackValue;
    }

    public void setProductCashbackValue(String productCashbackValue) {
        this.productCashbackValue = productCashbackValue;
    }
}
