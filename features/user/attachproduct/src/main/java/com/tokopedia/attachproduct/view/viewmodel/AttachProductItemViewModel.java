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
    protected String shopName;

    public AttachProductItemViewModel(String productUrl, String productName, int productId,
                                      String productImageFull, String productImage,
                                      String productPrice, String shopName) {
        this.productUrl = productUrl;
        this.productName = productName;
        this.productId = productId;
        this.productImageFull = productImageFull;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.shopName = shopName;
    }

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

    public String getShopName() {
        return shopName;
    }

    @Override
    public int type(AttachProductListAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productUrl);
        dest.writeString(this.productName);
        dest.writeInt(this.productId);
        dest.writeString(this.productImageFull);
        dest.writeString(this.productImage);
        dest.writeString(this.productPrice);
        dest.writeString(this.shopName);
    }

    protected AttachProductItemViewModel(Parcel in) {
        this.productUrl = in.readString();
        this.productName = in.readString();
        this.productId = in.readInt();
        this.productImageFull = in.readString();
        this.productImage = in.readString();
        this.productPrice = in.readString();
        this.shopName = in.readString();
    }

    public static final Creator<AttachProductItemViewModel> CREATOR = new
            Creator<AttachProductItemViewModel>() {
        @Override
        public AttachProductItemViewModel createFromParcel(Parcel source) {return new AttachProductItemViewModel(source);}

        @Override
        public AttachProductItemViewModel[] newArray(int size) {return new AttachProductItemViewModel[size];}
    };
}
