package com.tokopedia.posapp.domain.model.product;

import com.tokopedia.posapp.domain.model.shop.ShopInfoDomain;

import java.util.List;

/**
 * Created by okasurya on 8/9/17.
 */
public class ProductDomain {
    private int productId;
    private String productName;
    private String productPrice;
    private double productPriceUnformatted;
    private String productUrl;
    private String productDescription;
    private String productImage;
    private String productImage300;
    private String productImageFull;

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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductPriceUnformatted() {
        return productPriceUnformatted;
    }

    public void setProductPriceUnformatted(double productPriceUnformatted) {
        this.productPriceUnformatted = productPriceUnformatted;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductImage300() {
        return productImage300;
    }

    public void setProductImage300(String productImage300) {
        this.productImage300 = productImage300;
    }

    public String getProductImageFull() {
        return productImageFull;
    }

    public void setProductImageFull(String productImageFull) {
        this.productImageFull = productImageFull;
    }
}
