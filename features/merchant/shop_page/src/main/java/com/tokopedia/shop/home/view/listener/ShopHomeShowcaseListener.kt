package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel

interface ShopHomeShowcaseListener {
    fun onNavigationBannerViewAllShowcaseClick(selectedShowcaseHeader: ShopHomeShowcaseUiModel.ShowcaseHeader)
    fun onNavigationBannerShowcaseClick(selectedShowcase: ShopHomeShowcaseUiModel.Tab.Showcase)
}
