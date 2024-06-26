package com.tokopedia.deals.analytics

object DealsAnalyticsConstants {

    object Event {
        const val CHECKOUT_PROGRESS = "checkout_progress"
        const val BEGIN_CHECKOUT = "begin_checkout"
        const val CLICK_DEALS = "clickDeals"
        const val PRODUCT_CLICK = "productClick"
        const val PRODUCT_VIEW = "productView"
        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
        const val VIEW_SEARCH_RESULT = "view_item_list/view_search_results"
        const val VIEW_DEALS_IRIS = "viewDealsIris"
        const val OPEN_SCREEN = "openScreen"
        const val VIEW_ITEM = "view_item"
        const val VIEW_ITEM_LIST = "view_item_list"
        const val ADD_TO_CART = "add_to_cart"
        const val SELECT_CONTENT = "select_content"
        const val EVENT_DEALS_CLICK = "digitalDealsClick"
        const val EVENT_CLICK_CHECK_LOCATION_PRODUCT_DETAIL = "click check location - product detail"
        const val EVENT_CLICK_CHECK_TNC_PRODUCT_DETAIL = "click check term and condition - product detail"
        const val EVENT_CLICK_CHECK_DESCRIPTION_PRODUCT_DETAIL =
            "check what you will get - product detail"
        const val EVENT_CLICK_CHECK_REDEEM_INS_PRODUCT_DETAIL = "check how to redeem - product detail"
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
        const val CHANGE_LOCATION_CATEGORY_PAGE = "change location - category page"
        const val SEARCH_BRAND_PAGE_CLICK = "click search - brand page"
        const val VIEW_SEARCH_RESULT_BRAND_PAGE = "impression on search results - brand page"
        const val CATEGORY_TAB_BRAND_PAGE = "click category tab - brand page"
        const val BRAND_POPULAR_IMPRESSION_BRAND_PAGE = "impression on brand popular - brand page"
        const val BANNER_IMPRESSION = "banner impression"
        const val CLICK_BANNER = "click banner"
        const val CLICK_VIEW_ALL_BANNER = "click view all banner"
        const val CLICK_CATEGORY_ICON = "click category icon"
        const val BRAND_POPULAR_IMPRESSION = "impression on brand popular"
        const val CLICK_ON_BRAND_POPULAR = "click on brand popular"
        const val CLICK_VIEW_ALL_BRAND_POPULAR = "click view all brand popular"
        const val CLICK_VIEW_ALL_BRAND_POPULAR_CATEGORY_PAGE = "click view all brand popular - category page"
        const val CLICK_VIEW_ALL_PRODUCT_CARD_HOME_PAGE = "click view all product card home page"
        const val CHANGE_LOCATION_HOME_PAGE = "change location - homepage"
        const val CLICK_ON_CURATED_CARD ="click on curated card"
        const val CLICK_SEARCH = "click search"
        const val CLICK_ON_PRODUCT_CARD_HOME_PAGE = "click on product card - %s"
        const val CLICK_VIEW_ALL_PRODUCT_CARD_HOME_PAGE2 = "click view all product card - %s"
        const val CLICK_ON_ORDER_LIST = "click on order list"
        const val CHIPS_CLICK_CATEGORY_PAGE = "click on chips - category page"
        const val APPLY_FILTER_CATEGORY_PAGE = "apply filter - category page"
        const val CLICK_ON_BRAND_POPULAR_CATEGORY_PAGE = "click on brand popular - category page"
        const val PRODUCT_CARD_CATEGORY_PAGE_IMPRESSION = "impression on product card - category page"
        const val IMPRESSION_ON_POPULAR_LANDMARK = "impression on popular landmarks"
        const val CLICK_ON_POPULAR_LANDMARK = "click on popular landmarks"
        const val IMPRESSION_ON_CURATED_CARD = "impression on curated card"
        const val IMPRESSION_ON_SEARCH_RESULT = "impression on search result"
        const val PRODUCT_CARD_HOME_PAGE_IMPRESSION = "impression on product card - %s"
        const val CATEGORY_TAB_CATEGORY_PAGE = "click category tab - category page"
        const val IMPRESSION_PRODUCT_BRAND = "impression product brand"
        const val CLICK_PRODUCT_BRAND = "click product brand"
        const val PDP_VIEW_PRODUCT = "view product detail"
        const val CLICK_BELI = "click beli"
        const val CLICK_RECOMMENDATION = "click on deals recommendation"
        const val IMPRESS_RECOMMENDATION = "impression on deals recommendation"
        const val CLICK_PROMO = "click promo"
        const val CLICK_PROCEED_PAYMENT = "click proceed to payment"
        const val CLICK_PAYMENT_OPTION = "click payment option button"
        const val VIEW_CHECKOUT = "view checkout"
        const val CART_PAGE_LOADED = "cart page loaded"
        const val CHECKOUT_STEP_1 = "1"
        const val CHECKOUT_STEP_2 = "2"
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
        const val BANNER_VIEW = "deals - %s - %s"
        const val CATEGORY_ICON_CLICK = "deals - %s - %s"
        const val BRAND_NAME_SCROLL = "deals - %s - %s"
        const val BRAND_CLICK = "deals - %s - %s"
        const val LOCATION_HOME_PAGE_CHANGE = "deals - %s - %s"
        const val CHANGE_LOCATION_CATEGORY_PAGE = "deals - %s - %s"
        const val CLICK_PRODUCT_HOMEPAGE = "deals - %s - %s - %s"
        const val CATEGORY_TAB_CLICK_ONLY_CATEGORY_NAME = "deals - %s"
        const val CATEGORY_FILTER_CHIPS_APPLIED = "deals - filter %s"
        const val POPULAR_LANDMARK_VIEW = "deals - %s - %s"
        const val POPULAR_LANDMARK_CLICK = "deals - %s - %s"
        const val CURATED_CARD_VIEW = "deals - %s - %s"
        const val SEARCH_RESULT_CASE_SHOWN = "deals - %s - %s"
        const val TWO_STRING_PATTERN = "%s - %s"
        const val THREE_STRING_PATTERN = "%s - %s - %s"
        const val FOUR_STRING_PATTERN = "%s - %s - %s - %s"
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
        const val attribution = "attribution"
        const val none = "none"
    }

    object Promotions {
        const val id = "id"
        const val name = "name"
        const val creative = "creative"
        const val creative_url = "creative_url"
        const val position = "position"
        const val category = "category"
    }

    object Products {
        const val name = "name"
        const val id = "id"
        const val price = "price"
        const val brand = "brand"
        const val category = "category"
        const val variant = "variant"
        const val list = "list"
        const val position = "position"
        const val attribution = "attribution"
    }

    object Impressions {
        const val name = "name"
        const val id = "id"
        const val price = "price"
        const val brand = "brand"
        const val category = "category"
        const val variant = "variant"
        const val list = "list"
        const val position = "position"
    }

    const val SCREEN_NAME = "screenName"
    const val CURRENT_SITE = "currentSite"
    const val CLIENT_ID = "clientId"
    const val SESSION_IRIS = "sessionIris"
    const val USER_ID = "userId"
    const val BUSINESS_UNIT = "businessUnit"
    const val CHECKOUT_STEP = "checkout_step"
    const val CHECKOUT_OPTION = "checkout_option"
    const val TOKOPEDIA_DIGITAL_DEALS = "tokopediadigitaldeals"
    const val TRAVELENTERTAINMENT_BU = "travel & entertainment"
    const val CATEGORY_LABEL = "category"
    const val DEALS = "deals"
    const val SLASH_DEALS = "/deals"
    const val BRAND = "brand"
    const val ECOMMERCE_LABEL = "ecommerce"
    const val CURRENCY_CODE = "currencyCode"
    const val IDR = "IDR"
    const val CLICK = "click"
    const val ACTION_FIELD = "actionField"
    const val PRODUCTS = "products"
    const val IMPRESSIONS = "impressions"
    const val NONE = "none/others"
    const val PROMOTIONS = "promotions"
    const val PRODUCT_CARD = "product card"
    const val DEALS_POPULAR_LANDMARK = "/deals/popular/landmarks"
    const val DEALS_CURATED_CARD = "/deals/curated/card"
    const val SEARCH_RESULT_LIST = "/deals - search result"
    const val FOOD_VOUCHER_LIST = "/deals"
    const val PRODUCT_HOME_PAGE_LIST = "/deals - product card - homepage"
    const val SCREEN_NAME_DEALS_PDP = "/digital/deals/product"
    const val SCREEN_NAME_DEALS_CHECKOUT = "/digital/deals/checkout"
    const val ITEMS = "items"
    const val ITEM_LIST = "item_list"
    const val ITEM_LIST_NAME = "item_list_name"
    const val ITEM_ID = "item_id"
    const val CATEGORY_ID = "category_id"
    const val QUANTITY = "quantity"
    const val CART_ID = "cart_id"
    const val SHOP_ID = "shop_id"
    const val SHOP_NAME = "shop_name"
    const val SHOP_TYPE = "shop_type"
    const val PRICE = "price"
    const val INDEX = "index"
    const val DIMENSION_40 = "dimension40"
    const val ITEM_NAME = "item_name"
    const val ITEM_BRAND = "item_brand"
    const val ITEM_VARIANT = "item_variant"
    const val ITEM_CATEGORY = "item_category"
    const val ZERO_STRING = "0"
    const val DASH_STRING = "-"
    const val PROMO = "promo"
    const val NON_PROMO = "non promo"
}
