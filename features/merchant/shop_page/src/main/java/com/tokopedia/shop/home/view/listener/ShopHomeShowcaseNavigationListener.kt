package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.ShopHomeShowcaseNavigationBannerWidgetAppearance

interface ShopHomeShowcaseNavigationListener {
    fun onNavigationBannerViewAllShowcaseClick(
        viewAllCtaAppLink: String,
        appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance,
        showcaseId: String
    )
    fun onNavigationBannerShowcaseClick(
        selectedShowcase: Showcase,
        uiModel: ShowcaseNavigationUiModel,
        tabCount: Int,
        tabName: String
    )
    fun onNavigationBannerImpression(
        uiModel: ShowcaseNavigationUiModel,
        tabCount: Int,
        tabName: String,
        showcaseId: String
    )
    fun onNavigationBannerTabClick(tabName: String)
}
