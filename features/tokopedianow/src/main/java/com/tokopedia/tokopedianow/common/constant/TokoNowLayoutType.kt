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
    TokoNowLayoutType.EDUCATIONAL_INFORMATION,
    TokoNowLayoutType.SHARING_EDUCATION,
    TokoNowLayoutType.SHARING_REFERRAL,
    TokoNowLayoutType.MAIN_QUEST,
    TokoNowLayoutType.MIX_LEFT_CAROUSEL,
    TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC,
    TokoNowLayoutType.PRODUCT_RECOM_OOC,
    TokoNowLayoutType.MEDIUM_PLAY_WIDGET,
    TokoNowLayoutType.SMALL_PLAY_WIDGET,
    TokoNowLayoutType.COUPON_CLAIM,
    TokoNowLayoutType.CHIP_CAROUSEL,
    TokoNowLayoutType.BUNDLING_WIDGET
)
annotation class TokoNowLayoutType {
    companion object {
        const val BANNER_CAROUSEL = "banner_carousel_v2"
        const val CATEGORY = "category_tokonow"
        const val LEGO_3_IMAGE = "lego_3_image"
        const val LEGO_6_IMAGE = "6_image"
        const val PRODUCT_RECOM = "top_carousel_tokonow"
        const val PRODUCT_RECOM_OOC = "product_recom_ooc"
        const val REPURCHASE_PRODUCT = "recent_purchase_tokonow"
        const val EDUCATIONAL_INFORMATION = "tokonow_usp"
        const val SHARING_EDUCATION = "tokonow_share"
        const val SHARING_REFERRAL = "tokonow_referral"
        const val MAIN_QUEST = "tokonow_main_quest"
        const val MIX_LEFT_CAROUSEL = "left_carousel"
        const val MIX_LEFT_CAROUSEL_ATC = "left_carousel_atc"
        const val MEDIUM_PLAY_WIDGET = "play_carousel"
        const val SMALL_PLAY_WIDGET = "play_carousel_small"
        const val COUPON_CLAIM = "coupon_claim"
        const val CHIP_CAROUSEL = "chip_carousel"
        const val BUNDLING_WIDGET = "bundling_widget"
    }
}
