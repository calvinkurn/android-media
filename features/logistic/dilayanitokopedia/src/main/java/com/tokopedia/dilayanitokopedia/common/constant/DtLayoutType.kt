package com.tokopedia.dilayanitokopedia.common.constant

import androidx.annotation.StringDef

/**
 * Type for layout that returned by Home Dynamic Channel query.
 * The layout type defined is based on Home Dynamic Channel GQL
 * response.
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    DtLayoutType.BANNER_CAROUSEL,
    DtLayoutType.LEGO_6_IMAGE,
    DtLayoutType.MIX_TOP_CAROUSEL,
    DtLayoutType.MIX_LEFT_CAROUSEL,
    DtLayoutType.FEATURED_SHOP,

    // for loading
    DtLayoutType.LOADING_RECOMMENDATION_FEED
)
annotation class DtLayoutType {
    companion object {

        const val BANNER_CAROUSEL = "banner_carousel_v2"
        const val MIX_TOP_CAROUSEL = "top_carousel"
        const val LEGO_6_IMAGE = "6_image"
        const val FEATURED_SHOP = "shop_widget"
        const val MIX_LEFT_CAROUSEL = "left_carousel"

        // additional loading
        const val LOADING_RECOMMENDATION_FEED = "loading_recommendation_feed"
    }
}
