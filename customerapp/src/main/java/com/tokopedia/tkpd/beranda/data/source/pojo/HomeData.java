package com.tokopedia.tkpd.beranda.data.source.pojo;

import com.google.gson.annotations.Expose;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeData {
    @Expose
    private DynamicHomeChannel dynamicHomeChannel;

    @Expose
    private Slides slides;

    @Expose
    private Ticker ticker;

    public DynamicHomeChannel getDynamicHomeChannel() {
        return dynamicHomeChannel;
    }

    public void setDynamicHomeChannel(DynamicHomeChannel dynamicHomeChannel) {
        this.dynamicHomeChannel = dynamicHomeChannel;
    }

    public Slides getSlides() {
        return slides;
    }

    public void setSlides(Slides slides) {
        this.slides = slides;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }
}
