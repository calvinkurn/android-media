package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.Showcase

interface ShopHomeShowcaseNavigationListener {
    fun onNavigationBannerViewAllShowcaseClick(viewAllCtaAppLink: String)
    fun onNavigationBannerShowcaseClick(selectedShowcase: Showcase)
}
