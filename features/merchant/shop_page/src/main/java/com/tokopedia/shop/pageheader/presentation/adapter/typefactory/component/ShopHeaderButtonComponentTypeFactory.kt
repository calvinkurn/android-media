package com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component

import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageOnlyComponentUiModel

interface ShopHeaderButtonComponentTypeFactory {
    fun type(model: ShopHeaderButtonComponentUiModel): Int
}