package com.tokopedia.tokopedianow.common.analytics

object TokoNowCommonAnalyticConstants {

    object EVENT{
        const val EVENT_VIEW_TOKONOW_IRIS = "viewTokoNowIris"
        const val EVENT_CLICK_TOKONOW = "clickTokoNow"
        const val EVENT_SELECT_CONTENT = "select_content"
        const val EVENT_VIEW_ITEM = "view_item"
        const val EVENT_VIEW_ITEM_LIST = "view_item_list"
        const val EVENT_PRODUCT_VIEW = "productView"
        const val EVENT_PRODUCT_CLICK = "productClick"
        const val EVENT_ATC = "addToCart"
        const val EVENT_ADD_TO_CART = "add_to_cart"
        const val EVENT_OPEN_SCREEN = "openScreen"
    }

    object CATEGORY{
        const val EVENT_CATEGORY_TOP_NAV = "tokonow - top nav"
    }

    object KEY {
        const val KEY_AFFINITY_LABEL = "affinityLabel"
        const val KEY_CURRENT_SITE = "currentSite"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_PROMOTIONS = "promotions"
        const val KEY_CREATIVE_NAME = "creative_name"
        const val KEY_CREATIVE_SLOT = "creative_slot"
        const val KEY_DIMENSION_104 = "dimension104"
        const val KEY_DIMENSION_38 = "dimension38"
        const val KEY_DIMENSION_79 = "dimension79"
        const val KEY_DIMENSION_82 = "dimension82"
        const val KEY_DIMENSION_49 = "dimension49"
        const val KEY_DIMENSION_40 = "dimension40"
        const val KEY_DIMENSION_45 = "dimension45"
        const val KEY_QUANTITY = "quantity"
        const val KEY_SHOP_ID = "shop_id"
        const val KEY_SHOP_NAME = "shop_name"
        const val KEY_SHOP_TYPE = "shop_type"
        const val KEY_USER_ID = "userId"
        const val KEY_PRODUCT_ID = "productId"
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_PAGE_SOURCE = "pageSource"
        const val KEY_INDEX = "index"
        const val KEY_ITEMS = "items"
        const val KEY_ITEM_ID = "item_id"
        const val KEY_ITEM_NAME = "item_name"
        const val KEY_ITEM_LIST = "item_list"
        const val KEY_ITEM_VARIANT = "item_variant"
        const val KEY_ITEM_BRAND = "item_brand"
        const val KEY_ITEM_CATEGORY = "item_category"
        const val KEY_BRAND = "brand"
        const val KEY_CATEGORY = "category"
        const val KEY_ID = "id"
        const val KEY_LIST = "list"
        const val KEY_NAME = "name"
        const val KEY_POSITION = "position"
        const val KEY_PRICE = "price"
        const val KEY_VARIANT = "variant"
        const val KEY_CURRENCY_CODE = "currencyCode"
        const val KEY_IMPRESSIONS = "impressions"
        const val KEY_ECOMMERCE = "ecommerce"
        const val KEY_CLICK = "click"
        const val KEY_ADD = "add"
        const val KEY_ACTION_FIELD = "actionField"
        const val KEY_PRODUCTS = "products"
        const val KEY_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        const val KEY_WAREHOUSE_ID = "warehouseId"
    }

    object VALUE {
        const val CURRENT_SITE_TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"
        const val BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"
        const val BUSINESS_UNIT_PHYSICAL_GOODS = "Physical Goods"
        const val LIST_HOME_PAGE_PAST_PURCHASE_WIDGET = "/tokonow - homepage - past_purchase_widget"
        const val CURRENCY_CODE_IDR = "IDR"
        const val SCREEN_NAME_TOKONOW_OOC = "tokonow ooc - "
    }

    object ACTION{
        const val EVENT_ACTION_CLICK_CHANGE_ADDRESS_ON_OOC = "click change address on ooc"
        const val EVENT_ACTION_CLICK_SHOP_ON_TOKOPEDIA = "click belanja di tokopedia on ooc"
    }
}