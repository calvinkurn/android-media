package com.tokopedia.digital.product.model;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class ProductDigitalData {
    private CategoryData categoryData;
    private BannerData bannerData;

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public BannerData getBannerData() {
        return bannerData;
    }

    public void setBannerData(BannerData bannerData) {
        this.bannerData = bannerData;
    }
}
