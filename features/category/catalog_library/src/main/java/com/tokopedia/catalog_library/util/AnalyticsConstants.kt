package com.tokopedia.catalog_library.util

interface EventKeys {
    companion object {
        const val VIEW_ITEM = "view_item"
        const val VIEW_ITEM_LIST = "view_item_list"
        const val TRACKER_ID = "trackerId"
        const val PROMOTIONS = "promotions"
        const val PAGE_PATH = "pagePath"
        const val SESSION_IRIS = "sessionIris"
        const val IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        const val SCREEN_NAME = "screenName"
        const val VIEW_CONTENT_IRIS = "viewContentIris"
        const val ITEMS = "items"
        const val ITEM_LIST = "item_list"
        const val CLICK_CONTENT = "clickContent"
        const val SELECT_CONTENT = "select_content"
        const val CATEGORY_ID = "categoryId"
        const val CATALOG_ID = "catalogId"

        const val BUSINESS_UNIT_VALUE = "Physical Goods"
        const val CURRENT_SITE_VALUE = "tokopediamarketplace"
        const val OPEN_SCREEN = "openScreen"

        const val INDEX = "index"
        const val ITEM_BRAND = "item_brand"
        const val ITEM_CATEGORY = "item_category"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val ITEM_VARIANT = "item_variant"
        const val PRICE = "price"

        const val CREATIVE_SLOT = "creative_slot"
        const val CREATIVE_NAME = "creative_name"
        const val DIMENSION61 = "dimension61"

        const val CREATIVE_NAME_SPECIAL_VALUE = "kategori spesial buatmu"
        const val CREATIVE_NAME_RELEVANT_VALUE = "cek katalognya produk incaranmu"
        const val CREATIVE_NAME_BRAND_VALUE = "cek katalognya produk incaranmu"
        const val CREATIVE_NAME_CATEGORY_LIST_VALUE = "brand terpopuler di katalog"

        const val KEY_EVENT = "event"
        const val KEY_EVENT_CATEGORY = "eventCategory"
        const val KEY_EVENT_ACTION = "eventAction"
        const val KEY_EVENT_LABEL = "eventLabel"
        const val KEY_USER_ID = "userId"

        const val KEY_ECOMMERCE = "ecommerce"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"

        const val PROMO_VIEW = "promoView"
        const val PRODUCT_VIEW = "productView"
        const val PRODUCT_CLICK = "productClick"
        const val CURRENCY_CODE = "currencyCode"
        const val IDR = "IDR"
        const val IMPRESSIONS = "impressions"
    }
}

interface CategoryKeys {
    companion object {
        const val CATALOG_LIBRARY_CATEGORY_PAGE = "catalog library - category page"
        const val CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA = "catalog library - category lihat semua"
        const val CATALOG_LIBRARY_LANDING_PAGE = "catalog library - landing page"
        const val CATALOG_LIBRARY_POPULAR_BRAND_PAGE = "catalog library - popular brand page"
        const val CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE = "catalog library - brand landing page"
    }
}

interface ActionKeys {
    companion object {
        const val IMPRESS_LIHAT_SEMUA_POPULAR_PAGE = "impress on lihat button"
        const val CLICK_CATEGORY_ON_BS = "click category on bottom sheet"
        const val IMPRESSION_ON_SPECIAL_CATEGORIES = "impression on special categories"
        const val IMPRESSION_ON_RELEVANT_CATALOGS = "impression on relevant catalogs"
        const val IMPRESSION_ON_POPULAR_BRANDS = "impression on popular brands"
        const val IMPRESSION_ON_CATALOG_LIST = "impression on catalog list"
        const val CLICK_CATEGORY_ON_SPECIAL_CATEGORIES = "click category on special categories"
        const val CLICK_CATALOG_ON_RELEVANT_CATALOGS = "click catalog on relevant catalogs"
        const val CLICK_BRAND_ON_POPULAR_BRANDS = "click brand on popular brands"
        const val CLICK_CATALOG_ON_CATALOG_LIST = "click catalog on catalog list"

        const val CLICK_LIHAT_SEMUA_ON_POPULAR_BRANDS = "click lihat semua on popular brands"
        const val CLICK_LIHAT_SEMUA_ON_SPECIAL_CATEGORIES =
            "click lihat semua on special categories"
        const val IMPRESSION_ON_CATEGORY_LIST = "impression on category list"
        const val IMPRESSION_ON_CATEGORY_LIST_BS = "impress category on bottom sheet"
        const val CLICK_ASCENDING_DESCENDING_SORT = "click ascending descending sort"
        const val CLICK_GRID_LIST_VIEW = "click grid list view"
        const val CLICK_CATEGORY_ON_CATEGORY_LIST = "click category on category list"
        const val CLICK_DROP_UP_BUTTON = "click drop up button"

        const val IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY =
            "impression on top 5 catalogs in category"
        const val IMPRESSION_ON_MOST_VIRAL_CATALOG_IN_CATEGORY =
            "impression on most viral catalog in category"
        const val IMPRESSION_ON_CATALOG_LIST_IN_CATEGORY = "impression on catalog list in category"
        const val IMPRESSION_ON_CATALOG = "impress catalog"
        const val CLICK_CATALOG_ON_TOP_5_CATALOGS_IN_CATEGORY =
            "click catalog on top 5 catalogs in category"
        const val CLICK_CATALOG_ON_MOST_VIRAL_CATALOG_IN_CATEGORY =
            "click on most viral catalog in category"
        const val CLICK_ON_CATALOG_LIST_IN_CATEGORY = "click on catalog list in category"
        const val CLICK_ON_CATALOG = "click catalog"
    }
}

interface TrackerId {
    companion object {
        const val IMPRESSION_ON_SPECIAL_CATEGORIES = "33775"
        const val IMPRESSION_ON_RELEVANT_CATALOGS = "33776"
        const val IMPRESSION_ON_POPULAR_BRANDS = "33777"
        const val IMPRESSION_ON_CATALOG_LIST = "33778"
        const val CLICK_CATEGORY_ON_SPECIAL_CATEGORIES = "33779"
        const val CLICK_CATALOG_ON_RELEVANT_CATALOGS = "33780"
        const val CLICK_BRAND_ON_POPULAR_BRANDS = "33781"
        const val CLICK_CATALOG_ON_CATALOG_LIST = "33782"
        const val CLICK_LIHAT_SEMUA_ON_SPECIAL_CATEGORIES = "33783"
        const val CLICK_LIHAT_SEMUA_ON_POPULAR_BRANDS = "33784"

        const val IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY = "33785"
        const val IMPRESSION_ON_MOST_VIRAL_CATALOG_IN_CATEGORY = "33786"
        const val IMPRESSION_ON_CATALOG_LIST_IN_CATEGORY = "33787"
        const val IMPRESSION_ON_CATALOG_LIST_BRAND = "37375"
        const val CLICK_CATALOG_ON_TOP_5_CATALOGS_IN_CATEGORY = "33788"
        const val CLICK_CATALOG_ON_MOST_VIRAL_CATALOG_IN_CATEGORY = "33789"
        const val CLICK_ON_CATALOG_LIST_IN_CATEGORY = "33790"
        const val CLICK_ON_CATALOG_LIST_IN_BRAND = "37376"
        const val OPEN_SCREEN_CLP = "37805"

        const val IMPRESSION_ON_CATEGORY_LIST = "33791"
        const val IMPRESSION_ON_CATEGORY_LIST_BRAND_LANDING = "37382"
        const val CLICK_ASCENDING_DESCENDING_SORT = "33792"
        const val CLICK_GRID_LIST_VIEW = "33793"
        const val CLICK_CATEGORY_ON_CATEGORY_LIST = "33794"
        const val CLICK_DROP_UP_BUTTON = "33795"
    }
}
