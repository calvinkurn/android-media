package com.tokopedia.attachproduct.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.attachproduct.view.adapter.AttachProductListAdapterTypeFactory;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductItemViewModel implements Parcelable, Visitable<AttachProductListAdapterTypeFactory>  {
    protected String productUrl;
    protected String productName;
    protected int productId;
    protected String productImageFull;
    protected String productImage;
    protected String productPrice;

    public AttachProductItemViewModel(String productUrl, String productName, int productId, String productImageFull, String productImage, String productPrice) {
        this.productUrl = productUrl;
        this.productName = productName;
        this.productId = productId;
        this.productImageFull = productImageFull;
        this.productImage = productImage;
        this.productPrice = productPrice;
    }

    public AttachProductItemViewModel(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttachProductItemViewModel> CREATOR = new Creator<AttachProductItemViewModel>() {
        @Override
        public AttachProductItemViewModel createFromParcel(Parcel in) {
            return new AttachProductItemViewModel(in);
        }

        @Override
        public AttachProductItemViewModel[] newArray(int size) {
            return new AttachProductItemViewModel[size];
        }
    };

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductImageFull() {
        return productImageFull;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    @Override
    public int type(AttachProductListAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
