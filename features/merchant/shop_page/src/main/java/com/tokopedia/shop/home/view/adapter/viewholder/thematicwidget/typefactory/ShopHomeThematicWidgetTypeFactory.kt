package com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.typefactory

import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ProductCardSeeAllUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ProductCardSpaceUiModel

interface ShopHomeThematicWidgetTypeFactory {
    fun type(uiModel: ShopHomeProductUiModel): Int
    fun type(uiModel: ProductCardSeeAllUiModel): Int
    fun type(uiModel: ProductCardSpaceUiModel): Int
}
