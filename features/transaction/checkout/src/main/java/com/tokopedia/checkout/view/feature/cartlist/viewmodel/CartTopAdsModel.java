package com.tokopedia.checkout.view.feature.cartlist.viewmodel;

public class CartTopAdsModel {
    int productId;
    int shopId;
    String productName;

    public CartTopAdsModel() {
    }

    public CartTopAdsModel(int productId, int shopId, String productName) {
        this.productId = productId;
        this.shopId = shopId;
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
