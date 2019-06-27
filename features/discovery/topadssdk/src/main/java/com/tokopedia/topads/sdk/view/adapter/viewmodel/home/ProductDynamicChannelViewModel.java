package com.tokopedia.topads.sdk.view.adapter.viewmodel.home;


import android.support.v7.widget.RecyclerView;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.view.adapter.factory.AdsTypeFactory;

public class ProductDynamicChannelViewModel implements Item<AdsTypeFactory> {

    private String productId;
    private String productName;
    private String productPrice;
    private String productCashback;
    private String productClickUrl;
    private String applink;
    private ProductImage productImage;

    @Override
    public int type(AdsTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int originalPos() {
        return RecyclerView.NO_POSITION;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductImage getProductImage() {
        return productImage;
    }

    public void setProductImage(ProductImage productImage) {
        this.productImage = productImage;
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

    public String getProductCashback() {
        return productCashback;
    }

    public void setProductCashback(String productCashback) {
        this.productCashback = productCashback;
    }

    public String getProductClickUrl() {
        return productClickUrl;
    }

    public void setProductClickUrl(String productClickUrl) {
        this.productClickUrl = productClickUrl;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }
}
