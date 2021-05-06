package com.tokopedia.topads.sdk.view.adapter.viewmodel.banner;

import com.tokopedia.kotlin.model.ImpressHolder;
import com.tokopedia.productcard.ProductCardModel;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopProductViewModel extends ImpressHolder implements Item<BannerAdsTypeFactory> {

    private ProductCardModel product;
    private CpmData cpmData;
    private final String appLink;
    private final String imageUrl;
    private final String adsClickUrl;

    public BannerShopProductViewModel(CpmData cpmData, ProductCardModel product, String appLink, String imageUrl, String adsClickUrl) {
        this.cpmData = cpmData;
        this.product = product;
        this.appLink = appLink;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductCardModel getProduct() {
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
