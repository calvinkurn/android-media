package com.tokopedia.core.product.model.productother;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.product.model.productotherace.ProductOtherAce;

/**
 * Created by ANGGA on 10/28/2015.
 */

@Deprecated
public class ProductOther implements Parcelable {

    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_image_no_square")
    @Expose
    private String productImageNoSquare;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_name")
    @Expose
    private String productName;

    public ProductOther() {
    }

    public ProductOther(ProductOtherAce data) {
        this.productId = data.getProductId();
        this.productPrice = data.getProductPrice();
        this.productImageNoSquare = data.getProductImage();
        this.productImage = data.getProductImage();
        this.productName = data.getProductName();
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

    public String getProductImageNoSquare() {
        return productImageNoSquare;
    }

    public void setProductImageNoSquare(String productImageNoSquare) {
        this.productImageNoSquare = productImageNoSquare;
    }

    protected ProductOther(Parcel in) {
        productPrice = in.readString();
        productId = in.readByte() == 0x00 ? null : in.readInt();
        productImageNoSquare = in.readString();
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
        dest.writeString(productImageNoSquare);
        dest.writeString(productImage);
        dest.writeString(productName);
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductOther> CREATOR = new Creator<ProductOther>() {
        @Override
        public ProductOther createFromParcel(Parcel in) {
            return new ProductOther(in);
        }

        @Override
        public ProductOther[] newArray(int size) {
            return new ProductOther[size];
        }
    };


    public static class Builder {
        private String productPrice;
        private Integer productId;
        private String productImageNoSquare;
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

        public Builder setProductImageNoSquare(String productImageNoSquare) {
            this.productImageNoSquare = productImageNoSquare;
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
            return anOtherProduct().setProductPrice(productPrice).setProductId(productId).setProductImageNoSquare(productImageNoSquare).setProductImage(productImage).setProductName(productName);
        }

        public ProductOther build() {
            ProductOther productOther = new ProductOther();
            productOther.setProductPrice(productPrice);
            productOther.setProductId(productId);
            productOther.setProductImageNoSquare(productImageNoSquare);
            productOther.setProductImage(productImage);
            productOther.setProductName(productName);
            return productOther;
        }
    }
}
