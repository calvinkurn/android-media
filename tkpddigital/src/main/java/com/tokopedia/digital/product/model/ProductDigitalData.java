package com.tokopedia.digital.product.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class ProductDigitalData {
    private CategoryData categoryData;
    private List<BannerData> bannerDataList = new ArrayList<>();

    public ProductDigitalData() {
    }

    private ProductDigitalData(Builder builder) {
        setCategoryData(builder.categoryData);
        setBannerDataList(builder.bannerDataList);
    }

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public List<BannerData> getBannerDataList() {
        return bannerDataList;
    }

    public void setBannerDataList(List<BannerData> bannerDataList) {
        this.bannerDataList = bannerDataList;
    }


    public static final class Builder {
        private CategoryData categoryData;
        private List<BannerData> bannerDataList = new ArrayList<>();

        public Builder() {
        }

        public Builder categoryData(CategoryData val) {
            categoryData = val;
            return this;
        }

        public Builder bannerDataList(List<BannerData> val) {
            bannerDataList = val;
            return this;
        }

        public ProductDigitalData build() {
            return new ProductDigitalData(this);
        }
    }
}
