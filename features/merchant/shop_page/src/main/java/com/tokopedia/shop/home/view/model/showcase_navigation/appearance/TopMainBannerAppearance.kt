package com.tokopedia.shop.home.view.model.showcase_navigation.appearance

import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseCornerShape

data class TopMainBannerAppearance(
    override val title: String,
    val showcases: List<Showcase>,
    override val viewAllCtaAppLink: String,
    override val cornerShape: ShowcaseCornerShape
) : ShopHomeShowcaseNavigationBannerWidgetAppearance
