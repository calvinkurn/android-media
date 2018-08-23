package com.tokopedia.shop.settings.etalase.view.adapter.factory;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleViewModel;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;

/**
 * Created by hendry on 20/08/18.
 */
public abstract class BaseShopEtalaseFactory extends BaseAdapterTypeFactory {
    public abstract int type(ShopEtalaseViewModel model);
    public abstract int type(ShopEtalaseTitleViewModel model);
}
