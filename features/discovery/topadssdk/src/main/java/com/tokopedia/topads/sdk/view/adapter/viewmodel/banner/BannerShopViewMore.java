package com.tokopedia.topads.sdk.view.adapter.viewmodel.banner;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory;

/**
 * Author errysuprayogi on 15,January,2020
 */
public class BannerShopViewMore implements Item<BannerAdsTypeFactory> {

    private final String appLink;

    public BannerShopViewMore(String appLink) {
        this.appLink = appLink;
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
}
