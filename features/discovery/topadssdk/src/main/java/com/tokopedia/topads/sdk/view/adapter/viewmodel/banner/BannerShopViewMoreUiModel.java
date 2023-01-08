package com.tokopedia.topads.sdk.view.adapter.viewmodel.banner;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory;

/**
 * Author errysuprayogi on 15,January,2020
 */
public class BannerShopViewMoreUiModel implements Item<BannerAdsTypeFactory> {

    private final CpmData cpmData;
    private final String appLink;
    private final String adsClickUrl;

    public BannerShopViewMoreUiModel(CpmData cpmData, String appLink, String adsClickUrl) {
        this.cpmData = cpmData;
        this.appLink = appLink;
        this.adsClickUrl = adsClickUrl;
    }

    @Override
    public int type(BannerAdsTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int originalPos() {
        return 0;
    }

    public String getAppLink() {
        return appLink;
    }

    public CpmData getCpmData() {
        return cpmData;
    }

    public String getAdsClickUrl() {
        return adsClickUrl;
    }
}
