package com.tokopedia.centralizedpromo.analytic

object CentralizedPromoConstant {
    const val EVENT_NAME_IMPRESSION = "viewAdsPromoIris"
    const val EVENT_NAME_CLICK = "clickAdsPromo"
    const val EVENT_CATEGORY_ADS_AND_PROMO = "ads and promotion"
    const val EVENT_NAME_VIEW_PG_IRIS = "viewPGIris"
    const val EVENT_NAME_CLICK_PG = "clickPG"

    // ON GOING
    const val EVENT_ACTION_ON_GOING_IMPRESSION = "impression your promotion"
    const val EVENT_ACTION_ON_GOING_CLICK = "click your promotion"

    // PROMO CREATION
    const val EVENT_ACTION_PROMO_CREATION_IMPRESSION = "impression form submission"
    const val EVENT_ACTION_PROMO_CREATION_CLICK = "click form submission"

    // POWER MERCHANT
    const val EVENT_CATEGORY_SELLER_APP = "tokopedia seller app"
    const val EVENT_CATEGORY_MAIN_APP = "tokopedia main app"

    const val EVENT_NAME_PM_IMPRESSION = "viewPowerMerchantIris"
    const val EVENT_NAME_PM_CLICK = "clickPowerMerchant"

    const val EVENT_ACTION_FREE_SHIPPING_IMPRESSION = "impression on BBO Banner"
    const val EVENT_ACTION_FREE_SHIPPING_CLICK = "click on BBO Banner"

    const val EVENT_LABEL_PM_ACTIVE = "PM Active"
    const val EVENT_LABEL_PM_INACTIVE = "PM Inactive"
    const val EVENT_LABEL_TRANSITION_PERIOD = "Transition Period"
    const val EVENT_LABEL_CHARGE_PERIOD = "Charge Period"

    // KEY
    const val KEY_SHOP_TYPE = "shop_type"
    const val KEY_USER_ID = "user_id"

    // MVC
    const val EVENT_ACTION_MVC_IMPRESSION = "impression detail submission mvc"
    const val EVENT_ACTION_MVC_CLICK_CLOSE = "click detail submission mvc - close"
    const val EVENT_ACTION_MVC_CLICK_CREATE = "click detail submission mvc - create"

    // MVC PRODUCT
    // https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/2759
    const val EVENT_ACTION_MVC_PRODUCT_IMPRESSION = "impression promotion card"
    const val EVENT_ACTION_MVC_PRODUCT_CLICK = "click kupon produk card"
    const val EVENT_ACTION_MVC_PRODUCT_IMPRESSION_BOTTOMSHEET =
        "impression kupon produk - bottom sheet"
    const val EVENT_ACTION_MVC_PRODUCT_CLICK_BOTTOMSHEET = "click buat kupon - bottom sheet"
    const val EVENT_ACTION_CLICK_PROMOTION_CARD = "click promotion card"
    const val EVENT_CATEGORY_MVC_PRODUCT = "seller dashboard - iklan dan promosi"
}