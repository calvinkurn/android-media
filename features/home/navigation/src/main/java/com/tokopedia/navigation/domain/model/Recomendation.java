package com.tokopedia.navigation.domain.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.navigation.presentation.adapter.InboxTypeFactory;
import com.tokopedia.topads.sdk.domain.model.ImpressHolder;

/**
 * Author errysuprayogi on 13,March,2019
 */
public class Recomendation extends ImpressHolder implements Visitable<InboxTypeFactory> {

    private int productId;
    private String productName = "";
    private String categoryBreadcrumbs = "";
    private String recommendationType = "";
    private String imageUrl = "";
    private String price = "";
    private String clickUrl = "";
    private String trackerImageUrl = "";
    private int priceNumber;
    private boolean isTopAds;

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

    public String getCategoryBreadcrumbs() {
        return categoryBreadcrumbs;
    }

    public void setCategoryBreadcrumbs(String categoryBreadcrumbs) {
        this.categoryBreadcrumbs = categoryBreadcrumbs;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getTrackerImageUrl() {
        return trackerImageUrl;
    }

    public void setTrackerImageUrl(String trackerImageUrl) {
        this.trackerImageUrl = trackerImageUrl;
    }

    public int getPriceNumber() {
        return priceNumber;
    }

    public void setPriceNumber(int priceNumber) {
        this.priceNumber = priceNumber;
    }

    public boolean isTopAds() {
        return isTopAds;
    }

    public void setTopAds(boolean topAds) {
        isTopAds = topAds;
    }

    @Override
    public int type(InboxTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
