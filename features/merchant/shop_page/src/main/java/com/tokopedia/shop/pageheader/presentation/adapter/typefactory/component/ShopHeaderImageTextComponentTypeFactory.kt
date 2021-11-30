package com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component

import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageTextComponentUiModel

interface ShopHeaderImageTextComponentTypeFactory {
    fun type(model: ShopHeaderImageTextComponentUiModel): Int
}