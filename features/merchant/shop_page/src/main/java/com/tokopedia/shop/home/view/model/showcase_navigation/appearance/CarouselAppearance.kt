package com.tokopedia.shop.home.view.model.showcase_navigation.appearance

import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase

data class CarouselAppearance(
    override val title: String,
    val showcases: List<Showcase>,
    override val viewAllCtaAppLink: String
) : ShopHomeShowcaseNavigationBannerWidgetAppearance
