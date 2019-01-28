package com.tokopedia.topads.sdk.view.adapter.viewmodel.banner;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopProductViewModel implements Item<BannerAdsTypeFactory> {

    private Product product;
    private final String appLink;
    private final String adsClickUrl;

    public BannerShopProductViewModel(Product product, String appLink, String adsClickUrl) {
        this.product = product;
        this.appLink = appLink;
        this.adsClickUrl = adsClickUrl;
    }

    public String getAppLink() {
        return appLink;
    }

    public String getAdsClickUrl() {
        return adsClickUrl;
    }


    public Product getProduct() {
        return product;
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
