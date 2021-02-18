package com.tokopedia.attachproduct.view.resultmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel;

/**
 * Created by Hendri on 19/02/18.
 */

public class ResultProduct implements Parcelable {
    private String productId;
    private String productUrl;
    private String productImageThumbnail;
    private String price;
    private String name;
    public ResultProduct(String productId, String productUrl, String productImageThumbnail, String price, String name) {
        this.productId = productId;
        this.productUrl = productUrl;
        this.productImageThumbnail = productImageThumbnail;
        this.price = price;
        this.name = name;
    }

    public ResultProduct(AttachProductItemUiModel viewModel){
        this.productId = viewModel.getProductId();
        this.productUrl = viewModel.getProductUrl();
        this.productImageThumbnail = viewModel.getProductImage();
        this.price = viewModel.getProductPrice();
        this.name = viewModel.getProductName();
    }

    public String getProductId() {
        return productId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductImageThumbnail() {
        return productImageThumbnail;
    }

    public String getPrice(){
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.productId);
        dest.writeString(this.productUrl);
        dest.writeString(this.productImageThumbnail);
        dest.writeString(this.price);
        dest.writeString(this.name);
    }

    protected ResultProduct(Parcel in) {
        this.productId = in.readString();
        this.productUrl = in.readString();
        this.productImageThumbnail = in.readString();
        this.price = in.readString();
        this.name = in.readString();
    }

    public static final Creator<ResultProduct> CREATOR = new Creator<ResultProduct>() {
        @Override
        public ResultProduct createFromParcel(Parcel source) {
            return new ResultProduct(source);
        }

        @Override
        public ResultProduct[] newArray(int size) {
            return new ResultProduct[size];
        }
    };
}
