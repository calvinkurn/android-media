package com.tokopedia.shop.home.view.model.showcase_navigation.appearance

import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseCornerShape
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseTab

data class LeftMainBannerAppearance(
    val tabs: List<ShowcaseTab>,
    override val title: String,
    override val viewAllCtaAppLink: String,
    override val cornerShape: ShowcaseCornerShape
) : ShopHomeShowcaseNavigationBannerWidgetAppearance


