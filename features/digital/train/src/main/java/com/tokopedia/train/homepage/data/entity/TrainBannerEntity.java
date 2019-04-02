package com.tokopedia.train.homepage.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainBannerEntity {
    @SerializedName("travelBanner")
    @Expose
    List<BannerDetail> banners;

    public List<BannerDetail> getBanners() {
        return banners;
    }
}
