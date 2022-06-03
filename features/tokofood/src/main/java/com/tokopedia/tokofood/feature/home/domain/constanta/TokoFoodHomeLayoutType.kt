package com.tokopedia.tokofood.feature.home.domain.constanta

import androidx.annotation.StringDef

/**
 * Layout type will be changed this is only listing
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    TokoFoodHomeLayoutType.TABS_TOKOFOOD,
    TokoFoodHomeLayoutType.USP_TOKOFOOD,
    TokoFoodHomeLayoutType.BANNER_CAROUSEL,
    TokoFoodHomeLayoutType.LEGO_6_IMAGE,
    TokoFoodHomeLayoutType.CATEGORY_WIDGET,
    TokoFoodHomeLayoutType.ICON_TOKOFOOD
)
annotation class TokoFoodHomeLayoutType {
    companion object {
        const val TABS_TOKOFOOD = "double_banner"
        const val USP_TOKOFOOD = "tokofood_usp"
        const val BANNER_CAROUSEL = "banner_carousel_v2"
        const val LEGO_6_IMAGE = "6_image"
        const val CATEGORY_WIDGET = "category_widget"
        const val ICON_TOKOFOOD = "home_icon"
    }
}