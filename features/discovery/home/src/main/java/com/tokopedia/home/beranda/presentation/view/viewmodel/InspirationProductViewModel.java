package com.tokopedia.home.beranda.presentation.view.viewmodel;

/**
 * Created by henrypriyono on 1/12/18.
 */

public class InspirationProductViewModel {
    private String recommedationType;
    private String productId;
    private String name;
    private String price;
    private String imageSource;
    private String url;
    private int page;
    private String priceInt;
    private String categoryBreadcrumb;

    public InspirationProductViewModel(String productId, String name, String price, String
            imageSource, String url, int page, String priceInt, String recommendationType,
                                       String categoryBreadcrumb) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageSource = imageSource;
        this.url = url;
        this.page = page;
        this.priceInt = priceInt;
        this.recommedationType = recommendationType;
        this.categoryBreadcrumb = categoryBreadcrumb;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPage() {
        return page;
    }

    public String getPriceInt() {
        return priceInt;
    }

    public void setPriceInt(String priceInt) {
        this.priceInt = priceInt;
    }

    public String getRecommedationType() {
        return recommedationType;
    }

    public void setRecommedationType(String recommedationType) {
        this.recommedationType = recommedationType;
    }

    public String getCategoryBreadcrumb() {
        return categoryBreadcrumb;
    }

    public void setCategoryBreadcrumb(String categoryBreadcrumb) {
        this.categoryBreadcrumb = categoryBreadcrumb;
    }
}
