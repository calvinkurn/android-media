package com.tokopedia.gm.subscribe.data.factory;

import com.tokopedia.gm.subscribe.data.source.product.GmSubscribeProductDataSource;
import com.tokopedia.gm.subscribe.data.source.product.GmSubscribeProductSelectorDataSource;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 2/2/17.
 */
public class GmSubscribeProductFactory {

    private final GmSubscribeProductDataSource gmSubscribeProductDataSource;

    @Inject
    public GmSubscribeProductFactory(GmSubscribeProductDataSource gmSubscribeProductDataSource) {
        this.gmSubscribeProductDataSource = gmSubscribeProductDataSource;
    }

    public GmSubscribeProductSelectorDataSource createGMSubscribeProductSource() {
        return new GmSubscribeProductSelectorDataSource(gmSubscribeProductDataSource);
    }
}
