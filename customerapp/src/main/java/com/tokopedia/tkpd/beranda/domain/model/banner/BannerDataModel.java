package com.tokopedia.tkpd.beranda.domain.model.banner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class BannerDataModel {
    @SerializedName("slides")
    private List<BannerSlidesModel> slides;

    public List<BannerSlidesModel> getSlides() {
        return slides;
    }

    public void setSlides(List<BannerSlidesModel> slides) {
        this.slides = slides;
    }

}
