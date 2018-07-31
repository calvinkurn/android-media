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

    private String productName;
    private String productPrice;
    private String productBrand;
    private String productCategory;
    private String productQty;
    private String productCategoryId;
    private String productAttribution;
    private String productVariant;

    public int getProductId() {
        return productId;
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

    public String getProductQty() {
        return productQty;
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

    private ProductDataCheckoutRequest(Builder builder) {
        productId = builder.productId;
        productName = builder.productName;
        productPrice = builder.productPrice;
        productBrand = builder.productBrand;
        productCategory = builder.productCategory;
        productQty = builder.productQty;
        productCategoryId = builder.productCategoryId;
        productAttribution = builder.productAttribution;
        productVariant = builder.productVariant;
    }

    public static final class Builder {
        private int productId;
        private String productName;
        private String productPrice;
        private String productBrand;
        private String productCategory;
        private String productQty;
        private String productCategoryId;
        private String productAttribution;
        private String productVariant;

        public Builder() {
        }

        public Builder productId(int val) {
            productId = val;
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

        public Builder productQty(String val) {
            productQty = val;
            return this;
        }

        public Builder productCategoryId(String val) {
            productCategoryId = val;
            return this;
        }

        public Builder productAttribution(String val) {
            productAttribution = val;
            return this;
        }

        public Builder productVariant(String val) {
            productVariant = val;
            return this;
        }

        public ProductDataCheckoutRequest build() {
            return new ProductDataCheckoutRequest(this);
        }
    }
}
