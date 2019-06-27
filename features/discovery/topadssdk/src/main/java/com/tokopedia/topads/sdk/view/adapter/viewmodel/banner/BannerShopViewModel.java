package com.tokopedia.topads.sdk.view.adapter.viewmodel.banner;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopViewModel implements Item<BannerAdsTypeFactory> {

    private final CpmData cpmData;
    private final String appLink;
    private final String adsClickUrl;

    public BannerShopViewModel(CpmData cpmData, String appLink, String adsClickUrl) {
        this.cpmData = cpmData;
        this.appLink = appLink;
        this.adsClickUrl = adsClickUrl;
    }

    public CpmData getCpmData() {
        return cpmData;
    }

    public String getAppLink() {
        return appLink;
    }

    public String getAdsClickUrl() {
        return adsClickUrl;
    }

    @Override
    public int type(BannerAdsTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int originalPos() {
        return 0;
    }
}
