package com.tokopedia.search.analytics

interface SearchEventTracking {
    interface Event {
        companion object {
            const val EVENT_VIEW_SEARCH_RESULT = "viewSearchResult"
            const val CLICK_SEARCH = "clickSearch"
            const val SEARCH_RESULT = "clickSearchResult"
            const val CLICK_WISHLIST = "clickWishlist"
            const val CLICK_TOP_NAV = "clickTopNav"
            const val VIEW_SEARCH_RESULT_IRIS = "viewSearchResultIris"
            const val CLICK_SEARCH_RESULT_IRIS = "clickSearchResultIris"
            const val PROMO_VIEW = "promoView"
            const val PROMO_CLICK = "promoClick"
            const val PRODUCT_VIEW = "productView"
            const val PRODUCT_CLICK = "productClick"
            const val CREATIVE = "creative"
        }
    }

    interface Category {
        companion object {
            const val EVENT_TOP_NAV = "top nav"
            const val EVENT_TOP_NAV_SEARCH_SRP = "top nav - search result page"
            const val SEARCH = "Search"
            const val FILTER_PRODUCT = "filter product"
            const val SEARCH_RESULT = "search result"
            const val GRID_MENU = "grid menu"
            const val SEARCH_TAB = "search tab"
            const val SORT = "Sort"
            const val SORT_BY = "sort by"
            const val TOP_NAV_SEARCH_RESULT_PAGE = "top nav - search result page"
        }
    }

    interface Action {
        companion object {
            const val QUICK_FILTER = "quick filter"
            const val CLICK_CHANGE_GRID = "click - "
            const val CLICK_CATALOG = "click - catalog"
            const val CLICK_TAB = "click - tab"
            const val NO_SEARCH_RESULT_WITH_TAB = "no search result - tab: %s"
            const val SORT_BY = "sort by"
            const val LONG_PRESS_PRODUCT = "click - long press product"
            const val ADD_WISHLIST = "add wishlist"
            const val REMOVE_WISHLIST = "remove wishlist"
            const val CLICK_CART_BUTTON_SEARCH_RESULT = "click cart button - search result"
            const val CLICK_HOME_BUTTON_SEARCH_RESULT = "click home button - search result"
            const val CLICK_SEARCH_BOX = "click search box"
            const val MODULE = "module"
            const val LOGIN = "login"
            const val NON_LOGIN = "nonlogin"
            const val IMPRESSION_BANNED_PRODUCT_TICKER_EMPTY = "impression - banned product ticker - empty"
            const val CLICK_BANNED_PRODUCT_TICKER_EMPTY = "click - banned product ticker - empty"
            const val IMPRESSION_BANNED_PRODUCT_TICKER_RELATED = "impression - banned product ticker - related"
            const val IMPRESSION_TICKER = "impression - ticker"
            const val CLICK_TICKER = "click - ticker"
            const val CLICK_BANNED_PRODUCT_TICKER_RELATED = "click - banned product ticker - related"
            const val GENERAL_SEARCH = "general search"
            const val CLICK_CHANGE_KEYWORD = "click ganti kata kunci"
            const val IMPRESSION_SHOP = "impression - shop"
            const val IMPRESSION_SHOP_ALTERNATIVE = "impression - shop - alternative"
            const val CLICK_SHOP = "click - shop"
            const val CLICK_SHOP_INACTIVE = "click - shop - inactive"
            const val CLICK_SHOP_ALTERNATIVE = "click - shop - alternative"
            const val IMPRESSION_PRODUCT_SHOP_TAB = "impression - product - shop tab"
            const val IMPRESSION_PRODUCT_SHOP_TAB_ALTERNATIVE = "impression - product - shop tab - alternative"
            const val CLICK_PRODUCT_SHOP_TAB = "click - product - shop tab"
            const val CLICK_PRODUCT_SHOP_TAB_ALTERNATIVE = "click - product - shop tab - alternative"
            const val IMPRESSION_INSPIRATION_CAROUSEL_PRODUCT = "impression - inspiration carousel product"
            const val IMPRESSION_INSPIRATION_CAROUSEL_INFO_PRODUCT = "impression - carousel banner"
            const val CLICK_INSPIRATION_CAROUSEL_SEARCH = "click - inspiration carousel search"
            const val CLICK_INSPIRATION_CAROUSEL_PRODUCT = "click - inspiration carousel product"
            const val CLICK_INSPIRATION_CAROUSEL_GRID_BANNER = "click lihat sekarang - carousel banner"
            const val CLICK = "click"
            const val CLICK_FUZZY_KEYWORDS_SUGGESTION = "click - fuzzy keywords - suggestion"
            const val IMPRESSION_BROAD_MATCH = "impression - broad match"
            const val CLICK_BROAD_MATCH_LIHAT_SEMUA = "click - broad match lihat semua"
            const val CLICK_BROAD_MATCH = "click - broad match"
            const val CLICK_INSPIRATION_CARD = "click inspiration card"
            const val CLICK_ADD_TO_CART_ON_PRODUCT_OPTIONS = "click add to cart on product options"
            const val CLICK_SHARE_PRODUCT_OPTIONS = "click - share - product options"
            const val IMPRESSION_INSPIRATION_CAROUSEL_CHIPS_PRODUCT = "impression - inspiration carousel chips product"
            const val CLICK_INSPIRATION_CAROUSEL_CHIPS_PRODUCT = "click - inspiration carousel chips product"
            const val CLICK_INSPIRATION_CAROUSEL_CHIPS_LIHAT_SEMUA = "click - inspiration carousel chips lihat semua"
            const val CLICK_INSPIRATION_CAROUSEL_CHIPS_VARIANT = "click - inspiration carousel chips variant"
        }
    }

    interface Label {
        companion object {
            const val KEYWORD_PRODUCT_ID = "Keyword: %s - product id: %s"
            const val TOPADS = "topads"
            const val GENERAL = "general"
            const val GENERAL_SEARCH_EVENT_LABEL = "%s|%s|%s|%s|%s|%s|%s"
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
            const val CURRENCY_CODE = "currencyCode"
            const val IDR = "IDR"
            const val ADD = "add"
            const val ACTION_FIELD = "actionField"
            const val PRODUCTS = "products"
        }
    }

    companion object {
        const val CURRENT_SITE = "currentSite"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val BUSINESS_UNIT = "businessUnit"
        const val SEARCH = "search"
        const val NONE = "none"
        const val OTHER = "other"
    }
}