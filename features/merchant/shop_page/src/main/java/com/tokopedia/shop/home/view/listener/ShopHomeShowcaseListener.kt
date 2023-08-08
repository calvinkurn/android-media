package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel

interface ShopHomeShowcaseListener {
    fun onViewAllShowcaseClick(selectedShowcaseHeader: ShopHomeShowcaseUiModel.ShowcaseHeader)
    fun onShowcaseClick(selectedShowcase: ShopHomeShowcaseUiModel.Tab.Showcase)
}
