package com.tokopedia.tkpd.feedplus.view.viewmodel;

/**
 * @author by nisie on 5/16/17.
 */

public class ProductFeedViewModel {

    private String name;
    private String price;
    private String imageSource;
    private String url;

    public ProductFeedViewModel(String produk1, String s, String s1) {
        this.name = produk1;
        this.price = s;
        this.imageSource = s1;
        this.url = "https://www.tokopedia.com/casemacbookjak/case-macbook-air-11-electric-blue-matte";
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
}
