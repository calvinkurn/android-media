package com.tokopedia.instantloan.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lavekush on 20/03/18.
 */

public class ResponseBannerOffer {
    @SerializedName("banner")
    @Expose
    private java.util.List<BannerEntity> banners;

    public List<BannerEntity> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerEntity> banners) {
        this.banners = banners;
    }

    @Override
    public String toString() {
        return "ResponseBannerOffer{" +
                "banners=" + banners +
                '}';
    }
}
