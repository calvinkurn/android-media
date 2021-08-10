package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel

interface ShopHomeShowcaseListWidgetListener {
    fun onShowcaseListWidgetItemClicked(showcaseItem: ShopHomeShowcaseListItemUiModel, showcaseId: String, position: Int)
}