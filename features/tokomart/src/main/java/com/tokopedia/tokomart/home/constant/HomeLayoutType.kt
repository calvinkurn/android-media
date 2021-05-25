package com.tokopedia.tokomart.home.constant

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    HomeLayoutType.BANNER_CAROUSEL,
    HomeLayoutType.CATEGORY,
    HomeLayoutType.LEGO_3_IMAGE
)
annotation class HomeLayoutType {
    companion object {
        const val BANNER_CAROUSEL = "banner_carousel_v2"
        const val CATEGORY = "category_tokonow"
        const val LEGO_3_IMAGE = "lego_3_image"
    }
}