package com.tokopedia.shop.settings.etalase.view.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleDataModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseDataModel
import com.tokopedia.shop.settings.etalase.data.TickerReadMoreDataModel

/**
 * Created by hendry on 20/08/18.
 */
abstract class BaseShopEtalaseFactory : BaseAdapterTypeFactory() {
    abstract fun type(model: ShopEtalaseDataModel): Int
    abstract fun type(model: ShopEtalaseTitleDataModel): Int
    abstract fun type(model: TickerReadMoreDataModel): Int

}
