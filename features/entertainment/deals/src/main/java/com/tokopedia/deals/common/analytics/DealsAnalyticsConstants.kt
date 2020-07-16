package com.tokopedia.deals.common.analytics

object DealsAnalyticsConstants {

    object Event {
        const val CLICK_DEALS = "clickDeals"
        const val PRODUCT_CLICK = "productClick"
        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
        const val VIEW_SEARCH_RESULT = "view_item_list/view_search_results"
        const val VIEW_DEALS_IRIS = "viewDealsIris"
    }

    object Category {
        const val DIGITAL_DEALS = "digital - deals"
    }

    object Action {
        const val CHANGE_LOCATION_SEARCH_PAGE = "change location - search page"
        const val VIEW_SEARCH_RESULT = "impression on search results"
        const val SEARCH_RESULT_CLICK = "click on search result"
        const val SEARCH_RESULT_BRAND_CLICK = "click on search result - brand page"
        const val CHIPS_IMPRESSION = "impression on chips"
        const val CHIPS_CLICK = "click on chips"
        const val LASTSEEN_IMPRESSION = "impression on last seen"
        const val LASTSEEN_CLICK = "click on last seen"
        const val CHANGE_LOCATION_BRAND_PAGE = "change location - brand page"
        const val SEARCH_BRAND_PAGE_CLICK = "click search - brand page"
        const val VIEW_SEARCH_RESULT_BRAND_PAGE = "impression on search results - brand page"
        const val CATEGORY_TAB_BRAND_PAGE = "click category tab - brand page"
        const val BRAND_POPULAR_IMPRESSION_BRAND_PAGE = "impression on brand popular - brand page"
    }

    object Label {
        const val CHANGE_LOCATION = "deals - %s - %s"
        const val VIEW_SEARCH_RESULT = "deals - %s - %s"
        const val SEARCH_RESULT_CLICK = "deals - product - %s - %s - %s"
        const val SEARCH_RESULT_BRAND_CLICK = "deals - brand - %s - %s"
        const val VIEW_SEARCH_RESULT_NOT_FOUND = "deals - %s - %s - result not found"
        const val VIEW_SEARCH_RESULT_BRAND_NOT_FOUND = "deals - %s - %s - %s - result not found"
        const val CHIPS_CLICK = "deals - %s"
        const val LASTSEEN_CLICK = "deals - %s"
        const val CATEGORY_TAB_CLICK = "deals - %s - %s"
    }

    object Item {
        const val name = "name"
        const val id = "id"
        const val price = "price"
        const val brand = "brand"
        const val category = "category"
        const val variant = "variant"
        const val list = "list"
        const val position = "position"
        const val attribution = ""
    }

    const val SCREEN_NAME = "screenName"
    const val CURRENT_SITE = "currentSite"
    const val CLIENT_ID = "clientId"
    const val SESSION_IRIS = "sessionIris"
    const val USER_ID = "userId"
    const val BUSINESS_UNIT = "businessUnit"
    const val TOKOPEDIA_DIGITAL_DEALS = "tokopediadigitaldeals"
    const val TRAVELENTERTAINMENT_BU = "travel &  entertainment"
    const val CATEGORY_LABEL = "category"
    const val DEALS = "deals"
    const val ECOMMERCE_LABEL = "ecommerce"
    const val CURRENCY_CODE = "currencyCode"
    const val IDR = "IDR"
    const val CLICK = "click"
    const val ACTION_FIELD = "actionField"
    const val PRODUCTS = "products"
    const val IMPRESSIONS = "impressions"
    const val NONE = "none/others"

    const val SEARCH_RESULT_LIST = "/deals - search result"
    const val FOOD_VOUCHER_LIST = "/food&voucher"
}