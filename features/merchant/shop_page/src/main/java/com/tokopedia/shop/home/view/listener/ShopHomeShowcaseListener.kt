package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel

interface ShopHomeShowcaseListener {
    fun onViewAllShowcaseClick(selectedShowcase: ShopHomeShowcaseUiModel.ShowcaseHeader)
    fun onShowcaseClick(selectedShowcase: ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase)
}
