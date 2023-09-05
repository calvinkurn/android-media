package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel

interface ShopHomeShowcaseNavigationListener {
    fun onNavigationBannerViewAllShowcaseClick(viewAllCtaAppLink: String)
    fun onNavigationBannerShowcaseClick(
        selectedShowcase: Showcase,
        uiModel: ShowcaseNavigationUiModel,
        tabCount: Int,
        tabName: String
    )
    fun onNavigationBannerImpression(uiModel: ShowcaseNavigationUiModel)
    fun onNavigationBannerTabClick(tabName: String)
}
