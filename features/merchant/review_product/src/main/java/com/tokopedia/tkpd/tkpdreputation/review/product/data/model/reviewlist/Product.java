
package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_image_url")
    @Expose
    private String productImageUrl;
    @SerializedName("product_page_url")
    @Expose
    private String productPageUrl;
    @SerializedName("product_price")
    @Expose
    private int productPrice;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    public void setProductPageUrl(String productPageUrl) {
        this.productPageUrl = productPageUrl;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

}
