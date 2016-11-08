package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 10/25/16. Tokopedia
 */

public class ProductCashback implements Parcelable{
    @SerializedName("product_cashback")
    @Expose
    private String productCashback;

    protected ProductCashback(Parcel in) {
        productCashback = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productCashback);
    }

    public void setProductCashback(String productCashback) {
        this.productCashback = productCashback;
    }

    public String getProductCashback() {
        return productCashback;
    }
}
