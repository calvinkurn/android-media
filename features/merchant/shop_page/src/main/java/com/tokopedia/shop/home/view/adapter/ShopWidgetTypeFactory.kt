package com.tokopedia.shop.home.view.adapter

import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel

interface ShopWidgetTypeFactory{
    fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int
}
