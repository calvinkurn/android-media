package com.tokopedia.home.beranda.domain.model.feed;

public class InspirationItemDomain {

    private final String id;

    private final String name;

    private final String url;

    private final String clickUrl;

    private final String appUrl;

    private final String imageUrl;

    private final String price;

    private final String recommendationType;

    private final String priceInt;

    private final String categoryBreadcrumb;

    public InspirationItemDomain(String id, String name, String url,
                                 String clickUrl, String appUrl, String imageUrl,
                                 String price, String recommendationType, String priceInt,
                                 String categoryBreadcrumb) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.clickUrl = clickUrl;
        this.appUrl = appUrl;
        this.imageUrl = imageUrl;
        this.price = price;
        this.recommendationType = recommendationType;
        this.priceInt = priceInt;
        this.categoryBreadcrumb = categoryBreadcrumb;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public String getPriceInt() {
        return priceInt;
    }

    public String getCategoryBreadcrumb() {
        return categoryBreadcrumb;
    }
}
