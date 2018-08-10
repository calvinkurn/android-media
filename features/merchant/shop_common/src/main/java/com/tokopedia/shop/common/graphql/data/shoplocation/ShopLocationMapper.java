package com.tokopedia.shop.common.graphql.data.shoplocation;

import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseViewModel;

import rx.functions.Func1;

/**
 * Created by hendry on 10/08/18.
 */

public class ShopLocationMapper implements Func1<ShopLocationModel, ShopLocationViewModel> {

    @Override
    public ShopLocationViewModel call(ShopLocationModel shopLocationModel) {
        return new ShopLocationViewModel(shopLocationModel);
    }
}
