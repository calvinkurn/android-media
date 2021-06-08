package com.tokopedia.buyerorderdetail.analytic.tracker

object BuyerOrderDetailTrackerConstant {
    // keys
    const val EVENT_KEY_BUSINESS_UNIT = "businessUnit"
    const val EVENT_KEY_CURRENT_SITE = "currentSite"
    const val EVENT_KEY_ENHANCED_ECOMMERCE = "ecommerce"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_ADD = "add"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCTS = "products"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_CATEGORY = "category"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_CATEGORY_ID = "category_id"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_DIMENSION_45 = "dimension45"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRODUCT_ID = "id"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_NAME = "name"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRICE = "price"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_QUANTITY = "quantity"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_ID = "shop_id"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_NAME = "shop_name"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_TYPE = "shop_type"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_CURRENCY_CODE = "currencyCode"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_ID = "productId"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_USER_ID = "userId"

    // event names
    const val EVENT_NAME_CLICK_PURCHASE_LIST = "clickPurchaseList"
    const val EVENT_NAME_ADD_TO_CART = "addToCart"

    // event categories
    const val EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP = "my purchase list detail - mp"

    // event actions
    const val EVENT_ACTION_PARTIAL_CLICK_ON_PRIMARY_BUTTON = "click on main button"
    const val EVENT_ACTION_PARTIAL_CLICK_ON_SECONDARY_BUTTON = "click on secondary button"
    const val EVENT_ACTION_PARTIAL_CLICK_ON_FINISH_ORDER_CONFIRMATION_DIALOG = "on finished order confirmation"
    const val EVENT_ACTION_CLICK_SEE_ORDER_HISTORY_DETAIL = "click lihat detail"
    const val EVENT_ACTION_CLICK_SEE_ORDER_INVOICE = "click lihat invoice"
    const val EVENT_ACTION_CLICK_COPY_ORDER_INVOICE = "click copy invoice number"
    const val EVENT_ACTION_CLICK_SHOP_NAME = "click shop name"
    const val EVENT_ACTION_CLICK_PRODUCT = "click product section"
    const val EVENT_ACTION_CLICK_SEE_SHIPMENT_TNC = "click lihat s&k on info pengiriman"
    const val EVENT_ACTION_CLICK_COPY_ORDER_AWB = "click copy no resi"
    const val EVENT_ACTION_CLICK_CHAT_ICON = "click chat button top right nav"
    const val EVENT_ACTION_CLICK_SEE_COMPLAINT = "click on lihat complain"
    const val EVENT_ACTION_CLICK_SIMILAR_PRODUCT = "click on product serupa"
    const val EVENT_ACTION_CLICK_BUY_AGAIN = "click on beli lagi"

    // event labels

    // business unit
    const val BUSINESS_UNIT_MARKETPLACE = "Seller Order Management"

    // current site
    const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"

    // separator
    const val SEPARATOR_STRIP = " - "
    const val SEPARATOR_COMMA = ", "

    // button name
    const val BUTTON_NAME_CHAT_SELLER = "chat seller"
    const val BUTTON_NAME_HELP = "bantuan"
    const val BUTTON_NAME_CANCEL_ORDER = "cancel order"
    const val BUTTON_NAME_TRACK_ORDER = "lacak"
    const val BUTTON_NAME_COMPLAINT_ORDER = "ajukan complain"
    const val BUTTON_NAME_VIEW_COMPLAINT_ORDER = "lihat complain"
    const val BUTTON_NAME_FINISH_ORDER = "selesaikan pesanan"
    const val BUTTON_NAME_REVIEW_ORDER = "beri ulasan"
}