package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatalogBannerBase {
    @Expose
    @SerializedName("slides")
    private List<CatalogBanner> banners;

    public List<CatalogBanner> getBanners() {
        return banners;
    }

    public void setBanners(List<CatalogBanner> banners) {
        this.banners = banners;
    }

    @Override
    public String toString() {
        return "CatalogBannerBase{" +
                "banners=" + banners +
                '}';
    }
}
