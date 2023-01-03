package com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component

import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel

interface ShopHeaderButtonComponentTypeFactory {
    fun type(model: ShopHeaderButtonComponentUiModel): Int
}
