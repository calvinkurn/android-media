package com.tokopedia.transactiondata.entity.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class ProductDataCheckoutRequest implements Parcelable {

    @SerializedName("product_id")
    @Expose
    public int productId;
    @SerializedName("is_ppp")
    @Expose
    public boolean isPurchaseProtection;
    @SerializedName("product_quantity")
    public int productQuantity;
    @SerializedName("product_notes")
    public String productNotes;

    private String productName;
    private String productPrice;
    private String productBrand;
    private String productCategory;
    private String productVariant;
    private String productShopId;
    private String productShopType;
    private String productShopName;
    private String productCategoryId;
    private String productListName;
    private String productAttribution;
    private long cartId;

    public ProductDataCheckoutRequest() {
    }

    private ProductDataCheckoutRequest(Builder builder) {
        productId = builder.productId;
        isPurchaseProtection = builder.isPurchaseProtection;
        productName = builder.productName;
        productPrice = builder.productPrice;
        productBrand = builder.productBrand;
        productCategory = builder.productCategory;
        productVariant = builder.productVariant;
        productQuantity = builder.productQuantity;
        productShopId = builder.productShopId;
        productShopType = builder.productShopType;
        productShopName = builder.productShopName;
        productCategoryId = builder.productCategoryId;
        productListName = builder.productListName;
        productAttribution = builder.productAttribution;
        cartId = builder.cartId;
        productNotes = builder.productNotes;
    }

    protected ProductDataCheckoutRequest(Parcel in) {
        productId = in.readInt();
        isPurchaseProtection = in.readByte() != 0;
        productQuantity = in.readInt();
        productNotes = in.readString();
        productName = in.readString();
        productPrice = in.readString();
        productBrand = in.readString();
        productCategory = in.readString();
        productVariant = in.readString();
        productShopId = in.readString();
        productShopType = in.readString();
        productShopName = in.readString();
        productCategoryId = in.readString();
        productListName = in.readString();
        productAttribution = in.readString();
        cartId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeByte((byte) (isPurchaseProtection ? 1 : 0));
        dest.writeInt(productQuantity);
        dest.writeString(productNotes);
        dest.writeString(productName);
        dest.writeString(productPrice);
        dest.writeString(productBrand);
        dest.writeString(productCategory);
        dest.writeString(productVariant);
        dest.writeString(productShopId);
        dest.writeString(productShopType);
        dest.writeString(productShopName);
        dest.writeString(productCategoryId);
        dest.writeString(productListName);
        dest.writeString(productAttribution);
        dest.writeLong(cartId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductDataCheckoutRequest> CREATOR = new Creator<ProductDataCheckoutRequest>() {
        @Override
        public ProductDataCheckoutRequest createFromParcel(Parcel in) {
            return new ProductDataCheckoutRequest(in);
        }

        @Override
        public ProductDataCheckoutRequest[] newArray(int size) {
            return new ProductDataCheckoutRequest[size];
        }
    };

    public int getProductId() {
        return productId;
    }

    public boolean isPurchaseProtection() {
        return isPurchaseProtection;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public String getProductAttribution() {
        return productAttribution;
    }

    public String getProductVariant() {
        return productVariant;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getProductShopId() {
        return productShopId;
    }

    public String getProductShopType() {
        return productShopType;
    }

    public String getProductShopName() {
        return productShopName;
    }

    public String getProductListName() {
        return productListName;
    }

    public long getCartId() {
        return cartId;
    }

    public static final class Builder {
        private int productId;
        private boolean isPurchaseProtection;
        private String productName;
        private String productPrice;
        private String productBrand;
        private String productCategory;
        private String productVariant;
        private int productQuantity;
        private String productShopId;
        private String productShopType;
        private String productShopName;
        private String productCategoryId;
        private String productListName;
        private String productAttribution;
        private long cartId;
        private String productNotes;

        public Builder() {
        }

        public Builder productId(int val) {
            productId = val;
            return this;
        }

        public Builder purchaseProtection(boolean val) {
            isPurchaseProtection = val;
            return this;
        }

        public Builder productName(String val) {
            productName = val;
            return this;
        }

        public Builder productPrice(String val) {
            productPrice = val;
            return this;
        }

        public Builder productBrand(String val) {
            productBrand = val;
            return this;
        }

        public Builder productCategory(String val) {
            productCategory = val;
            return this;
        }

        public Builder productVariant(String val) {
            productVariant = val;
            return this;
        }

        public Builder productQuantity(int val) {
            productQuantity = val;
            return this;
        }

        public Builder productShopId(String val) {
            productShopId = val;
            return this;
        }

        public Builder productShopType(String val) {
            productShopType = val;
            return this;
        }

        public Builder productShopName(String val) {
            productShopName = val;
            return this;
        }

        public Builder productCategoryId(String val) {
            productCategoryId = val;
            return this;
        }

        public Builder productListName(String val) {
            productListName = val;
            return this;
        }

        public Builder productAttribution(String val) {
            productAttribution = val;
            return this;
        }

        public Builder cartId(long val) {
            cartId = val;
            return this;
        }

        public Builder productNotes(String val) {
            productNotes = val;
            return this;
        }

        public ProductDataCheckoutRequest build() {
            return new ProductDataCheckoutRequest(this);
        }
    }
}
