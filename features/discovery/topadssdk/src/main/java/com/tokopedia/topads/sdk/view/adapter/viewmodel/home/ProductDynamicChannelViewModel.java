package com.tokopedia.topads.sdk.view.adapter.viewmodel.home;


import android.support.v7.widget.RecyclerView;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.view.adapter.factory.AdsTypeFactory;

public class ProductDynamicChannelViewModel implements Item<AdsTypeFactory> {

    private String imageUrl;
    private String impressionUrl;
    private String productName;
    private String productPrice;
    private String productCashback;

    @Override
    public int type(AdsTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int originalPos() {
        return RecyclerView.NO_POSITION;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImpressionUrl() {
        return impressionUrl;
    }

    public void setImpressionUrl(String impressionUrl) {
        this.impressionUrl = impressionUrl;
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
}
