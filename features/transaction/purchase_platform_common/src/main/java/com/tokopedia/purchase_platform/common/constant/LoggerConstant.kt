package com.tokopedia.purchase_platform.common.constant

object LoggerConstant {

    object Tag {
        const val P2_BUYER_FLOW_CART = "BUYER_FLOW_CART"
    }

    object Key {
        const val TYPE = "type"
        const val MESSAGE = "message"
        const val STACK_TRACE = "stack_trace"
        const val PRODUCT_ID_LIST = "product_id_list"
        const val IS_OCS = "is_ocs"
        const val IS_TRADE_IN = "is_trade_in"
        const val IS_TRADE_IN_INDOPAKET = "is_trade_in_indopaket"
        const val PRODUCT_ID = "product_id"
        const val REQUEST = "request"
    }

    object Type {
        const val LOAD_CART_PAGE_ERROR = "load_cart_page_error"
        const val UPDATE_CART_FOR_CHECKOUT_ERROR = "update_cart_for_checkout_error"
        const val LOAD_CHECKOUT_PAGE_ERROR = "load_checkout_page_error"
        const val LOAD_COURIER_ERROR = "load_courier_error"
        const val CHECKOUT_ERROR = "checkout_error"
    }

}