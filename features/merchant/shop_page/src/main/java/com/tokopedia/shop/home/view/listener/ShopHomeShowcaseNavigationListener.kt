package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel

interface ShopHomeShowcaseNavigationListener {
    fun onNavigationBannerViewAllShowcaseClick(viewAllCtaAppLink: String)
    fun onNavigationBannerShowcaseClick(selectedShowcase: Showcase, uiModel: ShowcaseNavigationUiModel)
    fun onNavigationBannerImpression(uiModel: ShowcaseNavigationUiModel)
}
