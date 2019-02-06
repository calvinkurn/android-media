package com.tokopedia.core.product.model.productotherace;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 19/01/2016.
 */

@Deprecated
public class ProductOtherAce implements Parcelable {

    @SerializedName("price")
    @Expose
    private String productPrice;
    @SerializedName("id")
    @Expose
    private Integer productId;
    @SerializedName("image_uri")
    @Expose
    private String productImage;
    @SerializedName("name")
    @Expose
    private String productName;

    public ProductOtherAce() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    protected ProductOtherAce(Parcel in) {
        productPrice = in.readString();
        productId = in.readByte() == 0x00 ? null : in.readInt();
        productImage = in.readString();
        productName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productPrice);
        if (productId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productId);
        }
        dest.writeString(productImage);
        dest.writeString(productName);
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductOtherAce> CREATOR = new Creator<ProductOtherAce>() {
        @Override
        public ProductOtherAce createFromParcel(Parcel in) {
            return new ProductOtherAce(in);
        }

        @Override
        public ProductOtherAce[] newArray(int size) {
            return new ProductOtherAce[size];
        }
    };


    public static class Builder {
        private String productPrice;
        private Integer productId;
        private String productImage;
        private String productName;

        private Builder() {
        }

        public static Builder anOtherProduct() {
            return new Builder();
        }

        public Builder setProductPrice(String productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder setProductId(Integer productId) {
            this.productId = productId;
            return this;
        }

        public Builder setProductImage(String productImage) {
            this.productImage = productImage;
            return this;
        }

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder but() {
            return anOtherProduct().setProductPrice(productPrice).setProductId(productId).setProductImage(productImage).setProductName(productName);
        }

        public ProductOtherAce build() {
            ProductOtherAce productOther = new ProductOtherAce();
            productOther.setProductPrice(productPrice);
            productOther.setProductId(productId);
            productOther.setProductImage(productImage);
            productOther.setProductName(productName);
            return productOther;
        }
    }
}
