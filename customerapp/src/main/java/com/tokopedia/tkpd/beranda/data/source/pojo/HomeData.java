package com.tokopedia.tkpd.beranda.data.source.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.beranda.domain.model.banner.BannerDataModel;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeData {
    @SerializedName("ticker")
    @Expose
    Ticker ticker;

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }
}
