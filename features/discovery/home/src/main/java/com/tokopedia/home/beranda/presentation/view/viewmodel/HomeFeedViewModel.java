package com.tokopedia.home.beranda.presentation.view.viewmodel;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory;

public class HomeFeedViewModel implements Visitable<HomeFeedTypeFactory> {
    public static final String DATA_NONE_OTHER = "none / other";
    public static final String DATA_NAME = "name";
    public static final String DATA_ID = "id";
    public static final String DATA_PRICE = "price";
    public static final String DATA_BRAND = "brand";
    public static final String DATA_VARIANT = "variant";
    public static final String DATA_CATEGORY = "category";
    public static final String DATA_LIST = "list";
    public static final String DATA_POSITION = "position";
    public static final String DATA_LIST_VALUE = "/ - p%s - %s - rekomendasi untuk anda - %s";
    public static final String DATA_LIST_VALUE_NON_LOGIN = "/ - p%s - non login - %s - rekomendasi untuk anda - %s";

    private final int position;
    private String productId;
    private String productName;
    private String categoryBreadcrumbs;
    private String recommendationType;
    private String imageUrl;
    private String price;
    private int priceNumber;

    public HomeFeedViewModel(String productId,
                             String productName,
                             String categoryBreadcrumbs,
                             String recommendationType,
                             String imageUrl,
                             String price,
                             int priceNumber,
                             int position) {
        this.productId = productId;
        this.productName = productName;
        this.categoryBreadcrumbs = categoryBreadcrumbs;
        this.recommendationType = recommendationType;
        this.imageUrl = imageUrl;
        this.price = price;
        this.priceNumber = priceNumber;
        this.position = position;
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

    public int getPosition() {
        return position;
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

    public int getPriceNumber() {
        return priceNumber;
    }

    public void setPriceNumber(int priceNumber) {
        this.priceNumber = priceNumber;
    }

    @Override
    public int type(HomeFeedTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Object convertFeedTabModelToImpressionDataForLoggedInUser(
            String tabName
    ) {
        return DataLayer.mapOf(
                DATA_NAME, getProductName(),
                DATA_ID, getProductId(),
                DATA_PRICE, getPriceNumber(),
                DATA_BRAND, DATA_NONE_OTHER,
                DATA_VARIANT, DATA_NONE_OTHER,
                DATA_CATEGORY, getCategoryBreadcrumbs(),
                DATA_LIST, String.format(
                        DATA_LIST_VALUE,
                        getPosition(),
                        tabName,
                        getRecommendationType()
                ),
                DATA_POSITION, getPosition());
    }

    public Object convertFeedTabModelToImpressionDataForNonLoginUser(
            String tabName
    ) {
        return DataLayer.mapOf(
                DATA_NAME, getProductName(),
                DATA_ID, getProductId(),
                DATA_PRICE, getPriceNumber(),
                DATA_BRAND, DATA_NONE_OTHER,
                DATA_VARIANT, DATA_NONE_OTHER,
                DATA_CATEGORY, getCategoryBreadcrumbs(),
                DATA_LIST, String.format(
                        DATA_LIST_VALUE_NON_LOGIN,
                        getPosition(),
                        tabName,
                        getRecommendationType()
                ),
                DATA_POSITION, getPosition());
    }

    public Object convertFeedTabModelToClickData() {
        return DataLayer.mapOf(
                DATA_NAME, getProductName(),
                DATA_ID, getProductId(),
                DATA_PRICE, getPriceNumber(),
                DATA_BRAND, DATA_NONE_OTHER,
                DATA_VARIANT, DATA_NONE_OTHER,
                DATA_CATEGORY, getCategoryBreadcrumbs(),
                DATA_POSITION, getPosition());
    }
}
