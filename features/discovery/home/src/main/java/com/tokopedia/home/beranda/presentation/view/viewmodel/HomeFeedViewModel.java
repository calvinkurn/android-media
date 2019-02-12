package com.tokopedia.home.beranda.presentation.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory;

public class HomeFeedViewModel implements Visitable<HomeFeedTypeFactory> {
    private String productId;
    private String productName;
    private String imageUrl;
    private String price;
    private boolean isTopAds;

    public HomeFeedViewModel(String productId,
                             String productName,
                             String imageUrl,
                             String price,
                             boolean isTopAds) {

        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.isTopAds = isTopAds;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public boolean isTopAds() {
        return isTopAds;
    }

    @Override
    public int type(HomeFeedTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
