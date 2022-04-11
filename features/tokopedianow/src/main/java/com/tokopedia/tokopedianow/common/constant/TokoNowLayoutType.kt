package com.tokopedia.tokopedianow.common.constant

import androidx.annotation.StringDef

/**
 * Type for layout that returned by Home Dynamic Channel query.
 * The layout type defined is based on Home Dynamic Channel GQL
 * response.
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    TokoNowLayoutType.BANNER_CAROUSEL,
    TokoNowLayoutType.CATEGORY,
    TokoNowLayoutType.LEGO_3_IMAGE,
    TokoNowLayoutType.LEGO_6_IMAGE,
    TokoNowLayoutType.PRODUCT_RECOM,
    TokoNowLayoutType.REPURCHASE_PRODUCT,
    TokoNowLayoutType.MAIN_QUEST
)
annotation class TokoNowLayoutType {
    companion object {
        const val BANNER_CAROUSEL = "banner_carousel_v2"
        const val CATEGORY = "category_tokonow"
        const val LEGO_3_IMAGE = "lego_3_image"
        const val LEGO_6_IMAGE = "6_image"
        const val PRODUCT_RECOM = "top_carousel_tokonow"
        const val REPURCHASE_PRODUCT = "recent_purchase_tokonow"
        const val EDUCATIONAL_INFORMATION = "tokonow_usp"
        const val SHARING_EDUCATION = "tokonow_share"
        const val MAIN_QUEST = "tokonow_main_quest"
        const val MIX_LEFT_CAROUSEL = "left_carousel"
    }
}