package com.tokopedia.autocompletecomponent.analytics

object AutoCompleteTrackingConstant {

    const val EVENT = "event"
    const val EVENT_CATEGORY = "eventCategory"
    const val EVENT_ACTION = "eventAction"
    const val EVENT_LABEL = "eventLabel"
    const val ECOMMERCE = "ecommerce"
    const val CAMPAIGN_CODE = "campaignCode"
    const val CLICK = "click"
    const val ACTION_FIELD = "actionField"
    const val LIST = "list"
    const val PRODUCTS = "products"
    const val PAGE_SOURCE = "pageSource"
    const val USER_ID = "userId"
    const val SCREEN_NAME = "screenName"
    const val CURRENT_SITE = "currentSite"
    const val BUSINESS_UNIT = "businessUnit"

    const val CURRENCYCODE = "currencyCode"
    const val IDR = "IDR"
    const val IMPRESSIONS = "impressions"
    const val PROMOVIEW = "promoView"
    const val PROMOTIONS = "promotions"

    const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    const val SEARCH = "search"
    const val BUSINESS_UNIT_PHYSICAL_GOODS = "Physical Goods"

    object Event {
        const val VIEW_TOP_NAV_IRIS = "viewTopNavIris"
        const val CLICK_TOP_NAV = "clickTopNav"
        const val CLICK_SEARCH = "clickSearch"
        const val PRODUCT_VIEW_IRIS = "productViewIris"
        const val PROMO_VIEW_IRIS = "promoViewIris"
        const val PRODUCT_CLICK = "productClick"
        const val CLICK_TOKO_NOW = "clickTokoNow"
        const val LONG_CLICK = "longClick"
    }

    object Category {
        const val TOP_NAV = "top nav"
        const val TOP_NAV_TOKO_NOW = "tokonow - top nav"
    }
}