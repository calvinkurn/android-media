package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase

interface ShopHomeShowcaseNavigationListener {
    fun onNavigationBannerViewAllShowcaseClick(viewAllCtaAppLink: String)
    fun onNavigationBannerShowcaseClick(selectedShowcase: Showcase)
}
