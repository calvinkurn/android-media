package com.tokopedia.tkpd.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class ProductCatalog implements Parcelable {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_price_fmt")
    @Expose
    private String productPriceFmt;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_uri")
    @Expose
    private String productUri;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_condition")
    @Expose
    private String productCondition;

    public String getProductId() {
        return productId;
    }

    public String getProductPriceFmt() {
        return productPriceFmt;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductUri() {
        return productUri;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductCondition() {
        return productCondition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.productPriceFmt);
        dest.writeString(this.productName);
        dest.writeString(this.productUri);
        dest.writeString(this.productPrice);
        dest.writeString(this.productCondition);
    }

    public ProductCatalog() {
    }

    protected ProductCatalog(Parcel in) {
        this.productId = in.readString();
        this.productPriceFmt = in.readString();
        this.productName = in.readString();
        this.productUri = in.readString();
        this.productPrice = in.readString();
        this.productCondition = in.readString();
    }

    public static final Parcelable.Creator<ProductCatalog> CREATOR = new Parcelable.Creator<ProductCatalog>() {
        @Override
        public ProductCatalog createFromParcel(Parcel source) {
            return new ProductCatalog(source);
        }

        @Override
        public ProductCatalog[] newArray(int size) {
            return new ProductCatalog[size];
        }
    };
}
