package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel

interface ShopHomeShowcaseListWidgetListener {
    fun onShowcaseListWidgetItemClicked(
        shopHomeShowcaseListSliderUiModel: ShopHomeShowcaseListSliderUiModel,
        showcaseItem: ShopHomeShowcaseListItemUiModel,
        position: Int,
        parentPosition: Int
    )
    fun onShowcaseListWidgetItemImpression(showcaseItem: ShopHomeShowcaseListItemUiModel, position: Int)
    fun onShowcaseListWidgetImpression(model: ShopHomeShowcaseListSliderUiModel, position: Int)
}
