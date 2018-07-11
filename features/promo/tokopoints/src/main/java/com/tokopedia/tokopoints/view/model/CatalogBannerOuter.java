package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatalogBannerOuter {
    @Expose
    @SerializedName("banners")
    private CatalogBannerBase bannerData;

    public CatalogBannerBase getBannerData() {
        return bannerData;
    }

    public void setBannerData(CatalogBannerBase bannerData) {
        this.bannerData = bannerData;
    }

    @Override
    public String toString() {
        return "CatalogBannerOuter{" +
                "bannerData=" + bannerData +
                '}';
    }
}
