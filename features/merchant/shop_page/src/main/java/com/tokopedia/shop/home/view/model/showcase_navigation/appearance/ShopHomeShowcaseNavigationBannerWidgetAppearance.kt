package com.tokopedia.shop.home.view.model.showcase_navigation.appearance

import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseCornerShape

sealed interface ShopHomeShowcaseNavigationBannerWidgetAppearance {
    val viewAllCtaAppLink: String
    val title: String
    val cornerShape: ShowcaseCornerShape
}
