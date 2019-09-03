package com.tokopedia.shop.settings.etalase.view.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleViewModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.data.TickerReadMoreViewModel

/**
 * Created by hendry on 20/08/18.
 */
abstract class BaseShopEtalaseFactory : BaseAdapterTypeFactory() {
    abstract fun type(model: ShopEtalaseViewModel): Int
    abstract fun type(model: ShopEtalaseTitleViewModel): Int
    abstract fun type(model: TickerReadMoreViewModel): Int

}
