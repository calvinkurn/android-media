package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeShowcaseNavigationUiModel

interface ShopHomeShowcaseNavigationListener {
    fun onViewAllShowcaseClick(selectedShowcaseHeader: ShopHomeShowcaseNavigationUiModel.ShowcaseHeader)
    fun onShowcaseClick(selectedShowcase: ShopHomeShowcaseNavigationUiModel.Tab.Showcase)
}
