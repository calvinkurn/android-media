package com.tokopedia.centralizedpromo.analytic

object CentralizedPromoConstant {
    const val EVENT_NAME_IMPRESSION = "viewAdsPromoIris"
    const val EVENT_NAME_CLICK = "clickAdsPromo"
    const val EVENT_CATEGORY_ADS_AND_PROMO = "ads and promotion"
    const val EVENT_NAME_VIEW_PG_IRIS = "viewPGIris"
    const val EVENT_NAME_CLICK_PG = "clickPG"

    //FILTER
    const val ID_FILTER_ALL = "0"
    const val EVENT_CLICK_FILTER_ALL= "click all features tab"
    const val TRACKER_ID_FILTER_ALL = "34633"

    const val EVENT_CLICK_FILTER_INCREASE_BUYER= "click increase buyer traffic tab"
    const val ID_FILTER_INCREASE_BUYER = "6"
    const val TRACKER_ID_INCREASE_BUYER = "34634"

    const val EVENT_CLICK_FILTER_INCREASE_NEW_ORDER= "click increase new order tab"
    const val ID_FILTER_INCREASE_NEW_ORDER = "7"
    const val TRACKER_ID_INCREASE_NEW_ORDER = "34635"

    const val EVENT_CLICK_FILTER_INCREASE_LOYALTY= "click increase loyalty tab"
    const val ID_FILTER_INCREASE_LOYALTY = "8"
    const val TRACKER_ID_INCREASE_LOYALTY= "34636"

    // PROMO LIST
    const val TRACKER_ID_IMPRESSION_CARD = "34637"
    const val EVENT_IMPRESSION_CARD = "impression card"

    const val EVENT_CLICK_CAMPAIGN_CARD = "click card"
    const val TRACKER_ID_CLICK_CARD = "34638"

    const val EVENT_IMPRESSION_BOTTOM_SHEET = "impression bottom sheet"
    const val TRACKER_ID_IMPRESSION_BOTTOM_SHEET= "34639"

    const val EVENT_BOTTOM_SHEET_CHECKBOX = "click bottom sheet - checkbox"
    const val TRACKER_ID_BOTTOM_SHEET_CHECKBOX = "34640"

    const val EVENT_BOTTOM_SHEET_CREATE_CAMPAIGN = "click bottom sheet - create campaign"
    const val TRACKER_ID_BOTTOM_SHEET_CREATE_CAMPAIGN = "34641"

    const val EVENT_BOTTOM_SHEET_PAYWALL= "click bottom sheet - paywall"
    const val TRACKER_ID_BOTTOM_SHEET_PAYWALL= "34642"

    const val IMPRESSION_BOTTOM_SHEET_PAYWALL= "impression bottom sheet - paywall"
    const val TRACKER_ID_IMPRESSION_BOTTOM_SHEET_PAYWALL= "34643"

    // ON GOING
    const val EVENT_ACTION_ON_GOING_IMPRESSION = "impression card - fitur tokomu"
    const val TRACKER_ID_ON_GOING_IMPRESSION = "34645"

    const val EVENT_ACTION_ON_GOING_CLICK = "click card - fitur tokomu"
    const val TRACKER_ID_ON_GOING_CLICK = "34644"

    // MVC
    const val EVENT_ACTION_MVC_IMPRESSION = "impression detail submission mvc"
    const val EVENT_ACTION_MVC_CLICK_CLOSE = "click detail submission mvc - close"
    const val EVENT_ACTION_MVC_CLICK_CREATE = "click detail submission mvc - create"

    // MVC PRODUCT
    // https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/2759
    const val EVENT_ACTION_MVC_PRODUCT_IMPRESSION_BOTTOMSHEET =
        "impression kupon produk - bottom sheet"
    const val EVENT_ACTION_MVC_PRODUCT_CLICK_BOTTOMSHEET = "click buat kupon - bottom sheet"
    const val EVENT_ACTION_CLICK_PROMOTION_CARD = "click promotion card"
    const val EVENT_CATEGORY_MVC_PRODUCT = "seller dashboard - iklan dan promosi"


}