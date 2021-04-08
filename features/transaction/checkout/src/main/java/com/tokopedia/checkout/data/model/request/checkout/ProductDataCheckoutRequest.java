package com.tokopedia.checkout.data.model.request.checkout;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class ProductDataCheckoutRequest implements Parcelable {

    @SuppressLint("Invalid Data Type")
    @SerializedName("product_id")
    @Expose
    public long productId;
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
    private String warehouseId;
    private String productWeight;
    private String promoCode;
    private String promoDetails;
    private String buyerAddressId;
    private String shippingDuration;
    private String courier;
    private String shippingPrice;
    private String codFlag;
    private String tokopediaCornerFlag;
    private String isFulfillment;
    private boolean isDiscountedPrice;
    private boolean isFreeShipping;
    private boolean isFreeShippingExtra;
    private int campaignId;

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
        warehouseId = builder.warehouseId;
        productWeight = builder.productWeight;
        promoCode = builder.promoCode;
        promoDetails = builder.promoDetails;
        buyerAddressId = builder.buyerAddressId;
        shippingDuration = builder.shippingDuration;
        courier = builder.courier;
        shippingPrice = builder.shippingPrice;
        codFlag = builder.codFlag;
        tokopediaCornerFlag = builder.tokopediaCornerFlag;
        isFulfillment = builder.isFulfillment;
        isDiscountedPrice = builder.isDiscountedPrice;
        isFreeShipping = builder.isFreeShipping;
        isFreeShippingExtra = builder.isFreeShippingExtra;
        campaignId = builder.campaignId;
    }

    protected ProductDataCheckoutRequest(Parcel in) {
        productId = in.readLong();
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
        warehouseId = in.readString();
        productWeight = in.readString();
        promoCode = in.readString();
        promoDetails = in.readString();
        buyerAddressId = in.readString();
        shippingDuration = in.readString();
        courier = in.readString();
        shippingPrice = in.readString();
        codFlag = in.readString();
        tokopediaCornerFlag = in.readString();
        isFulfillment = in.readString();
        isDiscountedPrice = in.readByte() != 0;
        isFreeShipping = in.readByte() != 0;
        campaignId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(productId);
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
        dest.writeString(warehouseId);
        dest.writeString(productWeight);
        dest.writeString(promoCode);
        dest.writeString(promoDetails);
        dest.writeString(buyerAddressId);
        dest.writeString(shippingDuration);
        dest.writeString(courier);
        dest.writeString(shippingPrice);
        dest.writeString(codFlag);
        dest.writeString(tokopediaCornerFlag);
        dest.writeString(isFulfillment);
        dest.writeByte((byte) (isDiscountedPrice ? 1 : 0));
        dest.writeByte((byte) (isFreeShipping ? 1 : 0));
        dest.writeInt(campaignId);
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

    public long getProductId() {
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

    public String getWarehouseId() {
        return warehouseId;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public String getPromoDetails() {
        return promoDetails;
    }

    public String getBuyerAddressId() {
        return buyerAddressId;
    }

    public String getShippingDuration() {
        return shippingDuration;
    }

    public String getCourier() {
        return courier;
    }

    public String getShippingPrice() {
        return shippingPrice;
    }

    public String getCodFlag() {
        return codFlag;
    }

    public String getTokopediaCornerFlag() {
        return tokopediaCornerFlag;
    }

    public String getIsFulfillment() {
        return isFulfillment;
    }

    public boolean isFreeShipping() {
        return isFreeShipping;
    }
    public boolean isFreeShippingExtra() {
        return isFreeShippingExtra;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public void setPromoDetails(String promoDetails) {
        this.promoDetails = promoDetails;
    }

    public void setBuyerAddressId(String buyerAddressId) {
        this.buyerAddressId = buyerAddressId;
    }

    public void setShippingDuration(String shippingDuration) {
        this.shippingDuration = shippingDuration;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public void setShippingPrice(String shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public void setTokopediaCornerFlag(String tokopediaCornerFlag) {
        this.tokopediaCornerFlag = tokopediaCornerFlag;
    }

    public boolean isDiscountedPrice() {
        return isDiscountedPrice;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public static final class Builder {
        private long productId;
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
        private String warehouseId;
        private String productWeight;
        private String promoCode;
        private String promoDetails;
        private String buyerAddressId;
        private String shippingDuration;
        private String courier;
        private String shippingPrice;
        private String codFlag;
        private String tokopediaCornerFlag;
        private String isFulfillment;
        private boolean isDiscountedPrice;
        private boolean isFreeShipping;
        private boolean isFreeShippingExtra;
        private int campaignId;

        public Builder() {
        }

        public Builder productId(long val) {
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

        public Builder warehouseId(String val) {
            warehouseId = val;
            return this;
        }

        public Builder productWeight(String val) {
            productWeight = val;
            return this;
        }

        public Builder promoCode(String val) {
            promoCode = val;
            return this;
        }

        public Builder promoDetails(String val) {
            promoDetails = val;
            return this;
        }

        public Builder buyerAddressId(String val) {
            buyerAddressId = val;
            return this;
        }

        public Builder shippingDuration(String val) {
            shippingDuration = val;
            return this;
        }

        public Builder courier(String val) {
            courier = val;
            return this;
        }

        public Builder shippingPrice(String val) {
            shippingPrice = val;
            return this;
        }

        public Builder codFlag(String val) {
            codFlag = val;
            return this;
        }

        public Builder tokopediaCornerFlag(String val) {
            tokopediaCornerFlag = val;
            return this;
        }

        public Builder isFulfillment(String val) {
            isFulfillment = val;
            return this;
        }

        public Builder setDiscountedPrice(boolean val) {
            isDiscountedPrice = val;
            return this;
        }

        public Builder isFreeShipping(boolean val) {
            isFreeShipping = val;
            return this;
        }

        public Builder isFreeShippingExtra(boolean val) {
            isFreeShippingExtra = val;
            return this;
        }

        public Builder campaignId(int val) {
            campaignId = val;
            return this;
        }

        public ProductDataCheckoutRequest build() {
            return new ProductDataCheckoutRequest(this);
        }
    }
}
