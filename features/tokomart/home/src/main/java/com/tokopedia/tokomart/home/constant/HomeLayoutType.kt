package com.tokopedia.tokomart.home.constant

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    HomeLayoutType.SECTION,
    HomeLayoutType.ALL_CATEGORY,
    HomeLayoutType.DYNAMIC_CHANNEL,
    HomeLayoutType.SLIDER_BANNER,
    HomeLayoutType.SLIDER_PRODUCT,
    HomeLayoutType.CHOOSE_ADDRESS_WIDGET
)
annotation class HomeLayoutType {
    companion object {
        const val SECTION = 0
        const val ALL_CATEGORY = 1
        const val DYNAMIC_CHANNEL = 2
        const val SLIDER_BANNER = 3
        const val SLIDER_PRODUCT = 4
        const val CHOOSE_ADDRESS_WIDGET = 5
    }
}