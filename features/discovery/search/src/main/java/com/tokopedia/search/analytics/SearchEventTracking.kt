package com.tokopedia.search.analytics

interface SearchEventTracking {
    interface Event {
        companion object {
            const val SEARCH_RESULT = "clickSearchResult"
            const val CLICK_TOP_NAV = "clickTopNav"
            const val VIEW_SEARCH_RESULT_IRIS = "viewSearchResultIris"
            const val CLICK_SEARCH_RESULT_IRIS = "clickSearchResultIris"
            const val PROMO_VIEW = "promoView"
            const val PROMO_CLICK = "promoClick"
            const val PRODUCT_VIEW = "productView"
            const val PRODUCT_CLICK = "productClick"
            const val CLICK_SEARCH = "clickSearch"
            const val ADD_TO_CART = "addToCart"
        }
    }

    interface Category {
        companion object {
            const val EVENT_TOP_NAV = "top nav"
            const val FILTER_PRODUCT = "filter product"
            const val SEARCH_RESULT = "search result"
            const val SEARCH_TAB = "search tab"
            const val SORT_BY = "sort by"
            const val TOP_NAV_SEARCH_RESULT_PAGE = "top nav - search result page"
            const val SEARCH_RESULT_PAGE = "search result page"
        }
    }

    interface Action {
        companion object {
            const val CLICK_PRODUCT = "click - product"
            const val IMPRESSION_PRODUCT = "impression - product"
            const val QUICK_FILTER = "quick filter"
            const val CLICK_TAB = "click - tab"
            const val SORT_BY = "sort by"
            const val LONG_PRESS_PRODUCT = "click - long press product"
            const val GENERAL_SEARCH = "general search"
            const val IMPRESSION_SHOP = "impression - shop"
            const val IMPRESSION_SHOP_ALTERNATIVE = "impression - shop - alternative"
            const val CLICK_SHOP = "click - shop"
            const val CLICK_SHOP_INACTIVE = "click - shop - inactive"
            const val CLICK_SHOP_ALTERNATIVE = "click - shop - alternative"
            const val IMPRESSION_PRODUCT_SHOP_TAB = "impression - product - shop tab"
            const val IMPRESSION_PRODUCT_SHOP_TAB_ALTERNATIVE = "impression - product - shop tab - alternative"
            const val CLICK = "click"
            const val CLICK_BROAD_MATCH = "click - broad match"
            const val CLICK_INSPIRATION_CARD = "click inspiration card"
            const val GENERAL_SEARCH_SHOP = "general search shop"
            const val CLICK_CAROUSEL_PRODUCT = "click - carousel product"
        }
    }

    interface Label {
        companion object {
            const val KEYWORD_PRODUCT_ID = "Keyword: %s - product id: %s"
            const val GENERAL_SEARCH_EVENT_LABEL = "%s|%s|%s|%s|%s|%s|%s"
            const val KEYWORD_FILTER = "keyword: %s - filter: %s"
        }
    }

    interface MOENGAGE {
        companion object {
            const val KEYWORD = "keyword"
            const val IS_RESULT_FOUND = "is_result_found"
            const val CATEGORY_ID_MAPPING = "category_id_mapping"
            const val CATEGORY_NAME_MAPPING = "category_name_mapping"
        }
    }

    interface EventMoEngage {
        companion object {
            const val SEARCH_ATTEMPT = "Search_Attempt"
        }
    }

    interface ECommerce {
        companion object {
            const val ECOMMERCE = "ecommerce"
            const val CURRENCY_CODE = "currencyCode"
            const val IDR = "IDR"
            const val ADD = "add"
            const val ACTION_FIELD = "actionField"
            const val PRODUCTS = "products"
            const val IMPRESSIONS = "impressions"
            const val CLICK = "click"
            const val LIST = "list"
            const val PROMOTIONS = "promotions"
        }
    }

    companion object {
        const val CURRENT_SITE = "currentSite"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val BUSINESS_UNIT = "businessUnit"
        const val SEARCH = "search"
        const val NONE = "none"
        const val OTHER = "other"
        const val PHYSICAL_GOODS = "physical goods"
        const val EXTERNAL_REFERENCE = "externalReference"
        const val ORGANIC = "organic"
        const val ORGANIC_ADS = "organic ads"
    }
}
