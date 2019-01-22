package com.tokopedia.transactiondata.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class ProductDataCheckoutRequest {

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
    }

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

        public ProductDataCheckoutRequest build() {
            return new ProductDataCheckoutRequest(this);
        }
    }
}
