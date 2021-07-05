package com.tokopedia.attachproduct.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 08/03/18.
 */

public class DataProductResponse {

    @SerializedName("url")
    @Expose
    private String productUrl;
    @SerializedName("name")
    @Expose
    private String productName;
    @SerializedName("id")
    @Expose
    private String productId;
    @SerializedName("image_url_700")
    @Expose
    private String productImageFull;
    @SerializedName("image_url")
    @Expose
    private String productImage;
    @SerializedName("price")
    @Expose
    private String productPrice;
    @SerializedName("shop")
    @Expose
    private DataShopResponse shop;

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImageFull() {
        return productImageFull;
    }

    public void setProductImageFull(String productImageFull) {
        this.productImageFull = productImageFull;
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

    public DataShopResponse getShop() {
        return shop;
    }
}
