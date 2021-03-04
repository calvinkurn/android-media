package com.tokopedia.shop.settings.etalase.view.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleUiModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseUiModel
import com.tokopedia.shop.settings.etalase.data.TickerReadMoreUiModel

/**
 * Created by hendry on 20/08/18.
 */
abstract class BaseShopEtalaseFactory : BaseAdapterTypeFactory() {
    abstract fun type(model: ShopEtalaseUiModel): Int
    abstract fun type(model: ShopEtalaseTitleUiModel): Int
    abstract fun type(model: TickerReadMoreUiModel): Int

}
