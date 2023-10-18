package com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component

import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderButtonComponentUiModel

interface ShopPageHeaderButtonComponentTypeFactory {
    fun type(model: ShopPageHeaderButtonComponentUiModel): Int
}
