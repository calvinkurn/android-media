package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel;

import com.tokopedia.topads.sdk.common.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.factory.BannerAdsTypeFactory;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopUiModel implements Item<BannerAdsTypeFactory> {

    private final CpmData cpmData;
    private final String appLink;
    private final String adsClickUrl;
    private final boolean isShowCta;

    public BannerShopUiModel(CpmData cpmData, String appLink, String adsClickUrl, boolean isShowCta) {
        this.cpmData = cpmData;
        this.appLink = appLink;
        this.adsClickUrl = adsClickUrl;
        this.isShowCta = isShowCta;
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

    public boolean isShowCta() {
        return isShowCta;
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
