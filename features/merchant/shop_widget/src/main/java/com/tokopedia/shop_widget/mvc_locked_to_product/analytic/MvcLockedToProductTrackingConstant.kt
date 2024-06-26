package com.tokopedia.shop_widget.mvc_locked_to_product.analytic

object MvcLockedToProductTrackingConstant {

    object Key {
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val KEY_ITEM_LIST = "item_list"
        const val KEY_SCREEN_NAME = "screenName"
        const val KEY_EVENT = "event"
        const val KEY_EVENT_CATEGORY = "eventCategory"
        const val KEY_EVENT_ACTION = "eventAction"
        const val KEY_EVENT_LABEL = "eventLabel"
        const val KEY_SHOP_ID = "shopId"
        const val KEY_USER_ID = "userId"
        const val KEY_PAGE_TYPE = "pageType"
        const val KEY_PRODUCT_ID = "productId"
        const val KEY_PAGE_SOURCE = "pageSource"
        const val KEY_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        const val KEY_ITEMS = "items"
        const val KEY_INDEX = "index"
        const val KEY_ITEM_BRAND = "item_brand"
        const val KEY_ITEM_CATEGORY = "item_category"
        const val KEY_ITEM_ID = "item_id"
        const val KEY_ITEM_NAME = "item_name"
        const val KEY_ITEM_VARIANT = "item_variant"
        const val KEY_PRICE = "price"
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_DIMENSION_45 = "dimension45"
        const val KEY_QUANTITY = "quantity"
        const val KEY_SHOP_UNDERSCORE_ID = "shop_id"
        const val KEY_DIMENSION_40 = "dimension40"
        const val KEY_DIMENSION_87 = "dimension87"
    }

    object Value {
        const val LOGIN = "login"
        const val NON_LOGIN = "non login"
        const val SHOP_PAGE_SELLER = "shop page - seller"
        const val SHOP_PAGE_BUYER = "shop page - buyer"
        const val CLICK_MVC_PRODUCT_CARD = "click mvc product card"
        const val CLICK_ATC_ON_MVC_PRODUCT_CARD = "click atc on mvc product card"
        const val SELECT_CONTENT = "select_content"
        const val ADD_TO_CART = "add_to_cart"
        const val MVC_PRODUCT_ITEM_LIST = "/shoppage - productmvc - %1s"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val PHYSICAL_GOODS = "Physical Goods"
        const val EVENT_OPEN_SCREEN = "openScreen"
        const val CLICK_PG = "clickPG"
        const val CLICK_MVC_SORT_OPTION = "click filter chip settings on mvc product page"
        const val CLICK_PRODUCT_QUANTITY = "click product quantity"
        const val CLICK_TRASH_BUTTON = "click trash button"
        const val ATC_UPDATE_ADD_PRODUCT_QUANTITY = "add - %1s"
        const val ATC_UPDATE_REMOVE_PRODUCT_QUANTITY = "remove - %1s"
        const val ATC_REMOVE_PRODUCT = "%1s - %2s"
        const val MVC_LOCKED_TO_PRODUCT_PAGE_TYPE = "/shoppage - productmvc"
        const val MVC_LOCKED_TO_PRODUCT_PAGE_SOURCE = "shop page product mvc - %1s - %2s"
        const val MVC_LOCKED_TO_PRODUCT_SCREEN_NAME = "$MVC_LOCKED_TO_PRODUCT_PAGE_TYPE - %1s"
        const val MVC_LOCKED_TO_PRODUCT_LIST_NAME = "/shoppage - productmvc"
        const val MVC_LOCKED_TO_PRODUCT_LIST_GENERAL_MODULE = "shop page"
        const val MVC_LOCKED_TO_PRODUCT_VIEW_PG_IRIS = "viewPGIris"
        const val MVC_LOCKED_TO_PRODUCT_VBS_IMPRESSION_ACTION = "mvc product variant bottom sheet impression"
    }

}