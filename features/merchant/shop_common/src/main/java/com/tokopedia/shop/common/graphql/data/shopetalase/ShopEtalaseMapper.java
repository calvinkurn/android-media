package com.tokopedia.shop.common.graphql.data.shopetalase;

import rx.functions.Func1;

/**
 * Created by hendry on 10/08/18.
 */

public class ShopEtalaseMapper implements Func1<ShopEtalaseModel, ShopEtalaseViewModel> {

    @Override
    public ShopEtalaseViewModel call(ShopEtalaseModel shopEtalaseModel) {
        return new ShopEtalaseViewModel(shopEtalaseModel);
    }
}
