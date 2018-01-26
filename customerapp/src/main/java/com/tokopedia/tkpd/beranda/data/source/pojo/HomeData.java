package com.tokopedia.tkpd.beranda.data.source.pojo;

import com.google.gson.annotations.Expose;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.beranda.domain.model.banner.BannerDataModel;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeData {
    @Expose
    private DynamicHomeChannel dynamicHomeChannel;

    @Expose
    private BannerDataModel slides;

    @Expose
    private Ticker ticker;

    public DynamicHomeChannel getDynamicHomeChannel() {
        return dynamicHomeChannel;
    }

    public void setDynamicHomeChannel(DynamicHomeChannel dynamicHomeChannel) {
        this.dynamicHomeChannel = dynamicHomeChannel;
    }

    public BannerDataModel getSlides() {
        return slides;
    }

    public void setSlides(BannerDataModel slides) {
        this.slides = slides;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }
}
