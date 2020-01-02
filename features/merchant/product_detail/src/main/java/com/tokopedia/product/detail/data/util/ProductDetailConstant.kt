package com.tokopedia.product.detail.data.util

import com.tokopedia.url.TokopediaUrl

object ProductDetailConstant {
    @JvmField
    var BASE_REST_URL = TokopediaUrl.getInstance().WS

    const val MAINAPP_SHOW_PDP_TOPADS = "mainapp_show_pdp_topads"

    //report product
    const val PATH_PRODUCT_TYPE = "v4/product/"
    const val PATH_REPORT_TYPE = "get_product_report_type.pl"

    const val PATH_PRODUCT_ACTION = "v4/action/product/"
    const val PATH_REPORT = "report_product.pl"

    const val PATH_FAVORITE_SHOP_ACTION = "v4/action/favorite-shop/fav_shop.pl"

    const val PARAM_PRODUCT_ID = "product_id"
    const val PARAM_REPORT_TYPE = "report_type"
    const val PARAM_TEXT_MESSAGE = "text_message"

    const val PARAM_PRODUCT_ETALASE_ID = "product_etalase_id"
    const val PARAM_PRODUCT_ETALASE_NAME = "product_etalase_name"

    const val VALUE_NEW_ETALASE = "new"

    //waarehouse product
    const val PATH_MOVE_TO_WAREHOUSE = "move_to_warehouse.pl"
    const val PATH_MOVE_TO_ETALASE = "edit_etalase.pl"

    const val PARAM_PRICE = "price"

    const val URL_VALUE_PROPOSITION_READY = "https://www.tokopedia.com/help/article/a-1937"
    const val URL_VALUE_PROPOSITION_ORI = "https://www.tokopedia.com/help/article/a-1938"
    const val URL_VALUE_PROPOSITION_GUARANTEE_7_DAYS = "https://www.tokopedia.com/help/article/a-1939"
    const val URL_VALUE_PROPOSITION_GUARANTEE = "https://www.tokopedia.com/help/article/a-1940"
    const val URL_YOUTUBE = "https://www.youtube.com/watch?v="


    const val WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition"
    const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"

    const val KEY_USER_ID = "userID"
    const val KEY_PAGE_NAME = "pageName"
    const val KEY_XDEVICE = "xDevice"
    const val DEFAULT_DEVICE = "android"
    const val DEFAULT_SRC_PAGE = "recommen_pdp"
    const val KEY_PRODUCT_ID = "productIDs"
    const val KEY_XSOURCE = "xSource"
    const val KEY_PAGE_NUMBER = "pageNumber"
    const val DEFAULT_PAGE_NUMBER = 1
    const val DEFAULT_PAGE_NAME = "pdp_1,pdp_2,pdp_3,pdp_4"

    const val PDP_P1_TRACE = "mp_pdp_p1"
    const val PDP_P2_TRACE = "mp_pdp_p2"
    const val PDP_P2_GENERAL_TRACE = "mp_pdp_p2_general"
    const val PDP_P2_LOGIN_TRACE = "mp_pdp_p2_login"
    const val PDP_P3_TRACE = "mp_pdp_p3"

    //PAYLOADS
    const val PAYLOAD_WISHLIST = 1
    const val PAYLOAD_COD = 2
    const val PAYLOAD_TRADEIN = 3
    const val PAYLOAD_TOOGLE_FAVORITE = 2
    const val PAYLOAD_TOOGLE_AND_FAVORITE_SHOP = 3

    //Request Code
    const val REQUEST_CODE_TALK_PRODUCT = 1
    const val REQUEST_CODE_EDIT_PRODUCT = 2
    const val REQUEST_CODE_LOGIN = 561
    const val REQUEST_CODE_MERCHANT_VOUCHER_DETAIL = 563
    const val REQUEST_CODE_MERCHANT_VOUCHER = 564
    const val REQUEST_CODE_ETALASE = 565
    const val REQUEST_CODE_NORMAL_CHECKOUT = 566
    const val REQUEST_CODE_ATC_EXPRESS = 567
    const val REQUEST_CODE_LOGIN_THEN_BUY_EXPRESS = 569
    const val REQUEST_CODE_REPORT = 570
    const val REQUEST_CODE_SHOP_INFO = 998
    const val REQUEST_CODE_IMAGE_PREVIEW = 999

    //Result
    const val SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE"
    const val SHOP_STICKY_LOGIN = "SHOP_STICKY_LOGIN"

    //Mapper
    const val SOCIAL_PROOF = "social_proof"
    const val PRODUCT_SNAPSHOT = "product_snapshot"
    const val SHOP_INFO = "shop_info"
    const val PRODUCT_INFO = "product_info"
    const val DISCUSSION = "discussion"
    const val MOST_HELPFUL_REVIEW = "most_helpful_review"
    const val TRADE_IN = "tradein"
    const val INFO = "info"
    const val SHOP_VOUCHER = "shop_voucher"
    const val SEPARATOR = "separator"
    const val VALUE_PROPOSITION = "value_prop"
    const val PRODUCT_LIST = "product_list"
    const val PDP_1 = "pdp_1"
    const val PDP_2 = "pdp_2"
    const val PDP_3 = "pdp_3"
    const val PDP_4 = "pdp_4"
    const val PRODUCT_LAST_SEEN = "product_last_seen"
    const val PRODUCT_VARIANT_INFO = "variant"
    const val PRODUCT_WHOLESALE_INFO = "wholesale"
    const val PRODUCT_INSTALLMENT_INFO = "installment"
    const val PRODUCT_SHIPPING_INFO = "shipping"
    const val ORDER_PRIORITY = "order_prio"
    const val PRODUCT_FULLFILMENT = "fulfillment"
    const val PRODUCT_PROTECTION = "protection"
    const val VALUE_PROP = "value_prop"

    //Arguments
    const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
    const val ARG_WAREHOUSE_ID = "ARG_WAREHOUSE_ID"
    const val ARG_PRODUCT_KEY = "ARG_PRODUCT_KEY"
    const val ARG_SHOP_DOMAIN = "ARG_SHOP_DOMAIN"
    const val ARG_TRACKER_ATTRIBUTION = "ARG_TRACKER_ATTRIBUTION"
    const val ARG_TRACKER_LIST_NAME = "ARG_TRACKER_LIST_NAME"
    const val ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK"
    const val ARG_FROM_AFFILIATE = "ARG_FROM_AFFILIATE"
    const val ARG_AFFILIATE_STRING = "ARG_AFFILIATE_STRING"
    const val ARG_DEEPLINK_URL = "ARG_DEEPLINK_URL"

    //Animation
    const val CART_MAX_COUNT = 99
    const val CART_ALPHA_ANIMATION_FROM = 1f
    const val CART_ALPHA_ANIMATION_TO = 0f
    const val CART_SCALE_ANIMATION_FROM = 1f
    const val CART_SCALE_ANIMATION_TO = 2f
    const val CART_SCALE_ANIMATION_PIVOT = 0.5f
    const val CART_ANIMATION_DURATION = 700L

    //Save Instance State
    const val SAVED_NOTE = "saved_note"
    const val SAVED_QUANTITY = "saved_quantity"
    const val SAVED_VARIANT = "saved_variant"

}
