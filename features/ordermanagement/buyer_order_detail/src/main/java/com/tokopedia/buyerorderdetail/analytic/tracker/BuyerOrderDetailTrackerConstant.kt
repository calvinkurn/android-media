package com.tokopedia.buyerorderdetail.analytic.tracker

object BuyerOrderDetailTrackerConstant {
    // keys
    const val EVENT_KEY_BUSINESS_UNIT = "businessUnit"
    const val EVENT_KEY_CURRENT_SITE = "currentSite"
    const val EVENT_KEY_TRACKER_ID = "trackerId"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCTS_ITEMS = "items"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_CATEGORY_ID = "category_id"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_DIMENSION_40 = "dimension40"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_DIMENSION_45 = "dimension45"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_BRAND = "item_brand"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_CATEGORY = "item_category"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRODUCT_ID = "item_id"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_NAME = "item_name"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_VARIANT = "item_variant"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRICE = "price"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_QUANTITY = "quantity"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_ID = "shop_id"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_NAME = "shop_name"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_TYPE = "shop_type"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_ID = "productId"
    const val EVENT_KEY_ENHANCED_ECOMMERCE_USER_ID = "userId"

    // event names
    const val EVENT_NAME_CLICK_PURCHASE_LIST = "clickPurchaseList"
    const val EVENT_NAME_ADD_TO_CART = "add_to_cart"
    const val EVENT_NAME_CLICK_PG = "clickPG"
    const val EVENT_NAME_VIEW_PG_IRIS = "viewPGIris"

    // event categories
    const val EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP = "my purchase list detail - mp"

    // event actions
    const val EVENT_ACTION_PARTIAL_CLICK = "click"
    const val EVENT_ACTION_PARTIAL_CLICK_ON_PRIMARY_BUTTON = "click on main button"
    const val EVENT_ACTION_PARTIAL_CLICK_ON_SECONDARY_BUTTON = "click on secondary button"
    const val EVENT_ACTION_PARTIAL_CLICK_ON_FINISH_ORDER_CONFIRMATION_DIALOG =
        "on finished order confirmation"
    const val EVENT_ACTION_CLICK_SEE_ORDER_HISTORY_DETAIL = "click lihat detail"
    const val EVENT_ACTION_CLICK_SEE_ORDER_INVOICE = "click lihat invoice"
    const val EVENT_ACTION_CLICK_POD_PREVIEW = "click lihat - bukti pengiriman"
    const val EVENT_ACTION_CLICK_COPY_ORDER_INVOICE = "click copy invoice number"
    const val EVENT_ACTION_CLICK_SHOP_NAME = "click shop name"
    const val EVENT_ACTION_CLICK_PRODUCT = "click product section"
    const val EVENT_ACTION_CLICK_SEE_SHIPMENT_TNC = "click lihat s&k on info pengiriman"
    const val EVENT_ACTION_CLICK_COPY_ORDER_AWB = "click copy no resi"
    const val EVENT_ACTION_CLICK_CHAT_ICON = "click chat button top right nav"
    const val EVENT_ACTION_CLICK_SEE_COMPLAINT = "click on lihat complain"
    const val EVENT_ACTION_CLICK_SIMILAR_PRODUCT = "click on product serupa"
    const val EVENT_ACTION_CLICK_BUY_AGAIN = "attempt click beli lagi"
    const val EVENT_ACTION_CLICK_BUY_AGAIN_SUCCESS = "click beli lagi success"
    const val EVENT_ACTION_IMPRESSION_INSURANCE_WIDGET = "impression - proteksi transaksi"
    const val EVENT_ACTION_CLICK_INSURANCE_WIDGET = "click on insurance button"
    const val EVENT_ACTION_CLICK_RESOLUTION_WIDGET = "click on resolution widget"

    // event labels
    const val EVENT_LABEL_ATTEMPT_BUY_AGAIN = "attempt - order_id: "
    const val EVENT_LABEL_BUY_AGAIN_SUCCESS = "success - order_id: "

    // business unit
    const val BUSINESS_UNIT_MARKETPLACE = "Seller Order Management"
    const val BUSINESS_UNIT_PHYSICAL_GOODS = "Physical Goods"

    // current site
    const val CURRENT_SITE_TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    const val CURRENT_SITE_MARKETPLACE = "Marketplace"

    // separator
    const val SEPARATOR_STRIP = " - "
    const val SEPARATOR_COMMA = ", "
    const val SEPARATOR_SPACE = " "

    // button name
    const val BUTTON_NAME_CHAT_SELLER = "chat seller"
    const val BUTTON_NAME_HELP = "bantuan"
    const val BUTTON_NAME_CANCEL_ORDER = "cancel order"
    const val BUTTON_NAME_TRACK_ORDER = "lacak"
    const val BUTTON_NAME_COMPLAINT_ORDER = "ajukan complain"
    const val BUTTON_NAME_VIEW_COMPLAINT_ORDER = "lihat complain"
    const val BUTTON_NAME_FINISH_ORDER = "selesaikan pesanan"
    const val BUTTON_NAME_FINISH_ORDER_CONFIRMATION_CONFIRM_FINISH_ORDER = "selesai"
    const val BUTTON_NAME_FINISH_ORDER_CONFIRMATION_REQUEST_COMPLAINT = "ajukan komplain"
    const val BUTTON_NAME_REVIEW_ORDER = "beri ulasan"
    const val BUTTON_NAME_SEE_POD = "lihat bukti pengiriman"
    const val BUTTON_NAME_RE_UPLOAD_PRESCRIPTION = "upload foto resep"
    const val BUTTON_NAME_CHECK_PRESCRIPTION = "cek resep"

    // tracker ID
    const val TRACKER_ID_RE_UPLOAD_PRESCRIPTION = "32743"
    const val TRACKER_ID_CHECK_PRESCRIPTION = "32744"
    const val TRACKER_ID_IMPRESSION_INSURANCE_WIDGET = "40081"
    const val TRACKER_ID_CLICK_INSURANCE_WIDGET = "37322"

    // others
    const val MARKER_ORDER_LIST_DETAIL_MARKETPLACE = "/order list detail - marketplace"

    // buyer order extension
    const val EVENT_ACTION_CONFIRMATION_ORDER_EXTENSION =
        "click on confirmation order extension button"
    const val EVENT_ACTION_REQUEST_ACTION_ORDER_EXTENSION =
        "order extension request action"
    const val EVENT_LABEL_ACCEPT_EXTENSION = "accept extension"
    const val EVENT_LABEL_REJECT_EXTENSION = "reject extension"
    const val UOH_SOURCE = "UOH"
    const val BOM_SOURCE = "BOM"
}
