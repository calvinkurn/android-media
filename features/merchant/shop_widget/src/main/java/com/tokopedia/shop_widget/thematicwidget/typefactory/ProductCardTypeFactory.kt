package com.tokopedia.shop_widget.thematicwidget.typefactory

import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSeeAllUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSpaceUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel

interface ProductCardTypeFactory {
    fun type(uiModel: ProductCardUiModel): Int
    fun type(uiModel: ProductCardSeeAllUiModel): Int
    fun type(uiModel: ProductCardSpaceUiModel): Int
}