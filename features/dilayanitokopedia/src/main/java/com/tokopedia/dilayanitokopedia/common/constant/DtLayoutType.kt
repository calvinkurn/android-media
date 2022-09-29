package com.tokopedia.dilayanitokopedia.common.constant

import androidx.annotation.StringDef

/**
 * Type for layout that returned by Home Dynamic Channel query.
 * The layout type defined is based on Home Dynamic Channel GQL
 * response.
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(


    //Sliding Banner
    DtLayoutType.BANNER_CAROUSEL,

    //Anchor Category Tab + Anchor Customize Tab
    DtLayoutType.CATEGORY,

    //Lego6
    DtLayoutType.LEGO_6_IMAGE,

    // Dilayani Tokopedia USP + USP Bottomsheet
    DtLayoutType.EDUCATIONAL_INFORMATION,


    //            Flash Sale
    //            Products Carousel / Product Recommendation
    //            Brand Recommendation
    //            Product Recommendation for Ads / List of Recommended Brand
    //            Mega Tab +List Product
    //            Coupon


    //not know yet
    DtLayoutType.LEGO_3_IMAGE,
    DtLayoutType.PRODUCT_RECOM,
    DtLayoutType.REPURCHASE_PRODUCT,
    DtLayoutType.SHARING_EDUCATION,
    DtLayoutType.SHARING_REFERRAL,
    DtLayoutType.MAIN_QUEST,
    DtLayoutType.MIX_LEFT_CAROUSEL,
    DtLayoutType.MIX_LEFT_CAROUSEL_ATC


)
annotation class DtLayoutType {
    companion object {


        //TODO - need to check return from be
        const val BANNER_CAROUSEL = "banner_carousel_v2"
        const val CATEGORY = "category_dilayani_tokopedia"

        const val LEGO_3_IMAGE = "lego_3_image"
        const val LEGO_6_IMAGE = "6_image"
        const val PRODUCT_RECOM = "top_carousel_tokonow"
        const val REPURCHASE_PRODUCT = "recent_purchase_tokonow"
        const val EDUCATIONAL_INFORMATION = "tokonow_usp"
        const val SHARING_EDUCATION = "tokonow_share"
        const val SHARING_REFERRAL = "tokonow_referral"
        const val MAIN_QUEST = "tokonow_main_quest"
        const val MIX_LEFT_CAROUSEL = "left_carousel"
        const val MIX_LEFT_CAROUSEL_ATC = "left_carousel_atc"
        const val MEDIUM_PLAY_WIDGET = "play_carousel"
        const val SMALL_PLAY_WIDGET = "play_carousel_small"
    }
}