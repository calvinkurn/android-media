package com.tokopedia.core.router.productdetail.passdata;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Angga.Prasetiyo on 23/10/2015.
 */
public class ProductPass implements Parcelable {
    private static final String TAG = ProductPass.class.getSimpleName();

    private String productId;
    private String productName;
    private String productPrice;
    private String productImage;
    private String shopName;
    private String productUri;
    private String productKey;
    private String shopDomain;
    private String adKey;
    private String adR;
    private String productDesc;
    private Bitmap picToShare;
    private long dateTimeInMilis;

    public ProductPass() {
        dateTimeInMilis = 0;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductUri() {
        return productUri;
    }

    public void setProductUri(String productUri) {
        this.productUri = productUri;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getAdKey() {
        return adKey;
    }

    public void setAdKey(String adKey) {
        this.adKey = adKey;
    }

    public String getAdR() {
        return adR;
    }

    public void setAdR(String adR) {
        this.adR = adR;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Bitmap getPicToShare() {
        return picToShare;
    }

    public void setPicToShare(Bitmap picToShare) {
        this.picToShare = picToShare;
    }

    public long getDateTimeInMilis() {
        return dateTimeInMilis;
    }

    public void setDateTimeInMilis(long dateTimeInMilis) {
        this.dateTimeInMilis = dateTimeInMilis;
    }

    public static Creator<ProductPass> getCREATOR() {
        return CREATOR;
    }

    protected ProductPass(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productPrice = in.readString();
        productImage = in.readString();
        shopName = in.readString();
        productUri = in.readString();
        productKey = in.readString();
        shopDomain = in.readString();
        adKey = in.readString();
        adR = in.readString();
        productDesc = in.readString();
        picToShare = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        dateTimeInMilis = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(productPrice);
        dest.writeString(productImage);
        dest.writeString(shopName);
        dest.writeString(productUri);
        dest.writeString(productKey);
        dest.writeString(shopDomain);
        dest.writeString(adKey);
        dest.writeString(adR);
        dest.writeString(productDesc);
        dest.writeValue(picToShare);
        dest.writeLong(dateTimeInMilis);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductPass> CREATOR = new Parcelable.Creator<ProductPass>() {
        @Override
        public ProductPass createFromParcel(Parcel in) {
            return new ProductPass(in);
        }

        @Override
        public ProductPass[] newArray(int size) {
            return new ProductPass[size];
        }
    };

    public boolean haveBasicData() {
        return (!productName.equals("") & !productPrice.equals("") & !productId.equals(""));
    }


    public static class Builder {
        private String productId = "";
        private String productName = "";
        private String productPrice = "";
        private String productImage = "";
        private String shopName = "";
        private String productUri = "";
        private String productKey = "";
        private String shopDomain = "";
        private String adKey = "";
        private String adR = "";
        private String productDesc = "";
        private Bitmap picToShare;
        private long dateTimeInMilis;

        private Builder() {
        }

        public static Builder aProductPass() {
            return new Builder();
        }

        public Builder setProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder setProductId(int productId) {
            this.productId = String.valueOf(productId);
            return this;
        }


        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setProductPrice(String productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder setProductImage(String productImage) {
            this.productImage = productImage;
            return this;
        }

        public Builder setShopName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public Builder setProductUri(String productUri) {
            this.productUri = productUri;
            return this;
        }

        public Builder setProductKey(String productKey) {
            this.productKey = productKey;
            return this;
        }

        public Builder setShopDomain(String shopDomain) {
            this.shopDomain = shopDomain;
            return this;
        }

        public Builder setAdKey(String adKey) {
            this.adKey = adKey;
            return this;
        }

        public Builder setAdR(String adR) {
            this.adR = adR;
            return this;
        }

        public Builder setProductDesc(String productDesc) {
            this.productDesc = productDesc;
            return this;
        }

        public Builder setPicToShare(Bitmap picToShare) {
            this.picToShare = picToShare;
            return this;
        }

        public Builder setDateTimeInMilis(long dateTimeInMilis){
            this.dateTimeInMilis = dateTimeInMilis;
            return this;
        }

        public Builder but() {
            return aProductPass().setProductId(productId).setProductName(productName).setProductPrice(productPrice).setProductImage(productImage).setShopName(shopName).setProductUri(productUri).setProductKey(productKey).setShopDomain(shopDomain).setAdKey(adKey).setAdR(adR).setProductDesc(productDesc).setPicToShare(picToShare).setDateTimeInMilis(dateTimeInMilis);
        }

        public ProductPass build() {
            ProductPass productPass = new ProductPass();
            productPass.setProductId(productId);
            productPass.setProductName(productName);
            productPass.setProductPrice(productPrice);
            productPass.setProductImage(productImage);
            productPass.setShopName(shopName);
            productPass.setProductUri(productUri);
            productPass.setProductKey(productKey);
            productPass.setShopDomain(shopDomain);
            productPass.setAdKey(adKey);
            productPass.setAdR(adR);
            productPass.setProductDesc(productDesc);
            productPass.setPicToShare(picToShare);
            productPass.setDateTimeInMilis(dateTimeInMilis);
            return productPass;
        }
    }
}
