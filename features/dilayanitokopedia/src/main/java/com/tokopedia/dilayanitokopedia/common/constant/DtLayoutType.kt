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
//    DtLayoutType.CATEGORY,
//    DtLayoutType.LEGO_3_IMAGE,
    DtLayoutType.LEGO_6_IMAGE,
    DtLayoutType.PRODUCT_RECOM,
//    DtLayoutType.REPURCHASE_PRODUCT,
//    DtLayoutType.EDUCATIONAL_INFORMATION,
//    DtLayoutType.SHARING_EDUCATION,
//    DtLayoutType.SHARING_REFERRAL,
//    DtLayoutType.MAIN_QUEST,
    DtLayoutType.MIX_LEFT_CAROUSEL,
    DtLayoutType.FEATURED_SHOP
//    DtLayoutType.MIX_LEFT_CAROUSEL_ATC
)
annotation class DtLayoutType {
    companion object {

        //list supported by BE
//        "
//        banner_carousel_v2,
//        page_usp,
//        top_carousel,
//        6_image,
//        shop_widget,
//        left_carousel,
//        ",


        const val BANNER_CAROUSEL = "banner_carousel_v2"

        //        const val EDUCATIONAL_INFORMATION = "tokonow_usp"
        const val PRODUCT_RECOM = "top_carousel"
        const val LEGO_6_IMAGE = "6_image"
        const val FEATURED_SHOP = "shop_widget"
        const val MIX_LEFT_CAROUSEL = "left_carousel"


        //        const val CATEGORY = "category_tokonow"
//        const val LEGO_3_IMAGE = "lego_3_image"
//        const val REPURCHASE_PRODUCT = "recent_purchase_tokonow"
//        const val SHARING_EDUCATION = "tokonow_share"
//        const val SHARING_REFERRAL = "tokonow_referral"
//        const val MAIN_QUEST = "tokonow_main_quest"
//        const val MIX_LEFT_CAROUSEL_ATC = "left_carousel_atc"
//        const val MEDIUM_PLAY_WIDGET = "play_carousel"
//        const val SMALL_PLAY_WIDGET = "play_carousel_small"

        //        remove later
        const val TYPE_DEBUG_DEFAULT = ""
    }
}

