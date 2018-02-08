package com.tokopedia.tkpd.deeplink.data.factory;


import com.tokopedia.seller.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.tkpd.deeplink.data.source.ShopInfoCloudSource;
import com.tokopedia.tkpd.deeplink.data.source.ShopInfoSource;

import javax.inject.Inject;

/**
 * Created by okasurya on 1/5/18.
 */

public class ShopInfoSourceFactory {
    private ShopApi shopApi;

    @Inject
    public ShopInfoSourceFactory(ShopApi shopApi) {
        this.shopApi = shopApi;
    }

    public ShopInfoSource cloud() {
        return new ShopInfoCloudSource(shopApi);
    }
}
