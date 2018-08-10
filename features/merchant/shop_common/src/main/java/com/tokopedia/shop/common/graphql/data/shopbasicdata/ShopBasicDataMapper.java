package com.tokopedia.shop.common.graphql.data.shopbasicdata;

import rx.functions.Func1;

/**
 * Created by hendry on 10/08/18.
 */

public class ShopBasicDataMapper implements Func1<ShopBasicDataModel, ShopBasicDataViewModel> {

    @Override
    public ShopBasicDataViewModel call(ShopBasicDataModel shopBasicDataModel) {
        return new ShopBasicDataViewModel(shopBasicDataModel);
    }
}
