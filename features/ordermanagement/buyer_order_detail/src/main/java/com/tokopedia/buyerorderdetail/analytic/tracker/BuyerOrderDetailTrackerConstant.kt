package com.tokopedia.buyerorderdetail.analytic.tracker

object BuyerOrderDetailTrackerConstant {
    // keys
    const val EVENT_KEY_BUSINESS_UNIT = "businessUnit"
    const val EVENT_KEY_CURRENT_SITE = "currentSite"

    // event names
    const val EVENT_NAME_CLICK_PURCHASE_LIST = "clickPurchaseList"

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

    // event labels

    // business unit
    const val BUSINESS_UNIT_MARKETPLACE = "Seller Order Management"

    // current site
    const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"

    // separator
    const val SEPARATOR_STRIP = " - "

    // button name
    const val BUTTON_NAME_CHAT_SELLER = "chat seller"
    const val BUTTON_NAME_HELP = "bantuan"
    const val BUTTON_NAME_CANCEL_ORDER = "cancel order"
    const val BUTTON_NAME_TRACK_ORDER = "lacak"
    const val BUTTON_NAME_COMPLAINT_ORDER = "ajukan complain"
    const val BUTTON_NAME_FINISH_ORDER = "selesaikan pesanan"
    const val BUTTON_NAME_REVIEW_ORDER = "beri ulasan"
}