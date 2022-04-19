package com.tokopedia.tokofood.home.domain.constanta

import androidx.annotation.StringDef

/**
 * Layout type will be changed this is only listing
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    TokoFoodLayoutType.TABS_TOKOFOOD,
    TokoFoodLayoutType.USP_TOKOFOOD,
    TokoFoodLayoutType.BANNER_CAROUSEL,
    TokoFoodLayoutType.LEGO_6_IMAGE,
    TokoFoodLayoutType.CATEGORY_WIDGET,
    TokoFoodLayoutType.MERCHANT_LIST
)
annotation class TokoFoodLayoutType {
    companion object {
        const val TABS_TOKOFOOD = "tabs_tokofood"
        const val USP_TOKOFOOD = "usp_tokofood"
        const val BANNER_CAROUSEL = "banner_carousel_v2"
        const val LEGO_6_IMAGE = "6_image"
        const val CATEGORY_WIDGET = "category_widget"
        const val MERCHANT_LIST = "merchant_list"
    }
}