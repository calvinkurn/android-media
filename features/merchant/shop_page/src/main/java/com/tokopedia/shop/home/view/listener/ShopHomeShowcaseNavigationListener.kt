package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeShowcaseNavigationUiModel

interface ShopHomeShowcaseNavigationListener {
    fun onNavigationBannerViewAllShowcaseClick(selectedShowcaseHeader: ShopHomeShowcaseNavigationUiModel.ShowcaseHeader)
    fun onNavigationBannerShowcaseClick(selectedShowcase: ShopHomeShowcaseNavigationUiModel.Tab.Showcase)
}
