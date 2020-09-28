package com.tokopedia.centralizedpromo.analytic

object CentralizedPromoConstant {
    const val EVENT_NAME_IMPRESSION = "viewAdsPromoIris"
    const val EVENT_NAME_CLICK = "clickAdsPromo"
    const val EVENT_CATEGORY_ADS_AND_PROMO = "ads and promotion"

    // ON GOING
    const val EVENT_ACTION_ON_GOING_IMPRESSION = "impression your promotion"
    const val EVENT_ACTION_ON_GOING_CLICK = "click your promotion"

    // PROMO CREATION
    const val EVENT_ACTION_PROMO_CREATION_IMPRESSION = "impression form submission"
    const val EVENT_ACTION_PROMO_CREATION_CLICK = "click form submission"

    // TIPS & TRICK
    const val EVENT_ACTION_EDUCATION_IMPRESSION = "impression education"
    const val EVENT_ACTION_EDUCATION_CLICK = "click education"

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
}