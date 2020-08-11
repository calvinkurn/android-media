package com.tokopedia.product.detail.data.util

import com.tokopedia.url.TokopediaUrl

object ProductDetailConstant {


    @JvmField
    var BASE_REST_URL = TokopediaUrl.getInstance().WS


    //report product
    const val PATH_PRODUCT_TYPE = "v4/product/"
    const val PATH_REPORT_TYPE = "get_product_report_type.pl"

    const val PATH_PRODUCT_ACTION = "v4/action/product/"
    const val PATH_REPORT = "report_product.pl"

    const val PARAM_PRODUCT_ID = "product_id"
    const val PARAM_REPORT_TYPE = "report_type"
    const val PARAM_TEXT_MESSAGE = "text_message"

    const val PARAM_PRODUCT_ETALASE_ID = "product_etalase_id"
    const val PARAM_PRODUCT_ETALASE_NAME = "product_etalase_name"

    const val PARAM_OPTION = "option"
    const val PARAM_INCLUDE_CAMPAIGN = "includeCampaign"

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
    const val KEY_PREORDER = "preorder"
    const val KEY_LEASING = "leasing"
    const val KEY_NORMAL_BUTTON = "normal"
    const val KEY_OCS_BUTTON = "ocs"
    const val KEY_OCC_BUTTON = "occ"
    const val KEY_OVO_DEALS = "ovo_deals"
    const val KEY_CHAT = "chat"
    const val KEY_BYME = "byme"
    const val KEY_BUTTON_PRIMARY = "primary"
    const val KEY_BUTTON_SECONDARY = "secondary"
    const val DEFAULT_PAGE_NUMBER = 1
    const val DEFAULT_X_SOURCE = "pdp"

    const val PDP_P1_TRACE = "mp_pdp_p1"
    const val PDP_P2_TRACE = "mp_pdp_p2"
    const val PDP_P2_GENERAL_TRACE = "mp_pdp_p2_general"
    const val PDP_P2_LOGIN_TRACE = "mp_pdp_p2_login"
    const val PDP_P3_TRACE = "mp_pdp_p3"

    const val PDP_RESULT_PLT_PREPARE_METRICS = "pdp_result_plt_prepare_metrics"
    const val PDP_RESULT_PLT_NETWORK_METRICS = "pdp_result_plt_network_metrics"
    const val PDP_RESULT_PLT_RENDER_METRICS = "pdp_result_plt_render_metrics"
    const val PDP_RESULT_TRACE = "pdp_result_trace"

    //PAYLOADS
    const val PAYLOAD_WISHLIST = 1
    const val PAYLOAD_P3 = 2
    const val PAYLOAD_TOOGLE_FAVORITE = 2
    const val PAYLOAD_TOOGLE_AND_FAVORITE_SHOP = 3
    const val PAYLOAD_UPDATE_IMAGE = 5
    const val PAYLOAD_VARIANT_COMPONENT = 1
    const val PAYLOAD_NOTIFY_ME = 1

    //Request Code
    const val REQUEST_CODE_EDIT_PRODUCT = 2
    const val REQUEST_CODE_LOGIN = 561
    const val REQUEST_CODE_MERCHANT_VOUCHER_DETAIL = 563
    const val REQUEST_CODE_MERCHANT_VOUCHER = 564
    const val REQUEST_CODE_ETALASE = 565
    const val REQUEST_CODE_REPORT = 570
    const val REQUEST_CODE_SHOP_INFO = 998
    const val REQUEST_CODE_IMAGE_PREVIEW = 999

    //Result
    const val SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE"
    const val SHOP_STICKY_LOGIN = "SHOP_STICKY_LOGIN"

    //Mapper
    const val SOCIAL_PROOF = "social_proof"
    const val MINI_SOCIAL_PROOF = "social_proof_mini"
    const val SHOP_INFO = "shop_info"
    const val MINI_SHOP_INFO = "shop_info_mini"
    const val PRODUCT_INFO = "product_info"
    const val DISCUSSION = "discussion"
    const val DISCUSSION_FAQ = "discussion_faq"
    const val MOST_HELPFUL_REVIEW = "most_helpful_review"
    const val TRADE_IN = "tradein"
    const val INFO = "info"
    const val SHOP_VOUCHER = "shop_voucher"
    const val VALUE_PROPOSITION = "value_prop"
    const val PRODUCT_LIST = "product_list"
    const val NOTIFY_ME = "teaser"
      const val TICKER_INFO = "ticker_info"
    const val UPCOMING_DEALS = "upcoming_deals"
    const val VARIANT_OPTIONS = "variant_options"
    const val VARIANT = "variant"
    const val SOCIAL_PROOF_PV = "social_proof_pv"
    const val PDP_1 = "pdp_1"
    const val PDP_2 = "pdp_2"
    const val PDP_3 = "pdp_3"
    const val PDP_4 = "pdp_4"
    const val PRODUCT_LAST_SEEN = "product_last_seen"
    const val PRODUCT_VARIANT_INFO = "variant"
    const val PRODUCT_WHOLESALE_INFO = "wholesale"
    const val PRODUCT_SHOP_CREDIBILITY = "shop_credibility"
    const val PRODUCT_INSTALLMENT_INFO = "installment"
    const val PRODUCT_SHIPPING_INFO = "shipping"
    const val ORDER_PRIORITY = "order_prio"
    const val PRODUCT_FULLFILMENT = "fulfillment"
    const val PRODUCT_PROTECTION = "protection"
    const val VALUE_PROP = "value_prop"
    const val PRODUCT_CONTENT = "product_content"
    const val PRODUCT_CONTENT_TITLE_TOP = "product_content_title_top"
    const val MEDIA = "product_media"
    const val LAYOUT_HEAD_N_SHOULDERS = "head n shoulders"
    const val LAYOUT_DEFAULT = "Default Layout"
    const val LAYOUT_TAIL_N_SHOULDERS = "tail n shoulders"
    const val BY_ME = "byme"


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
    const val REVIEW_PRD_NM = "x_prd_nm"


    //Animation
    const val CART_MAX_COUNT = 99
    const val CART_ALPHA_ANIMATION_FROM = 1f
    const val CART_ALPHA_ANIMATION_TO = 0f
    const val CART_SCALE_ANIMATION_FROM = 1f
    const val CART_SCALE_ANIMATION_TO = 2f
    const val CART_SCALE_ANIMATION_PIVOT = 0.5f
    const val CART_ANIMATION_DURATION = 700L

    //Save Instance State
    const val SAVED_ACTIVITY_RESULT = "saved_activity_result"

    //Button Action
    const val BUY_BUTTON = 1
    const val ATC_BUTTON = 2
    const val OCS_BUTTON = 3
    const val OCC_BUTTON = 4
    const val LEASING_BUTTON = 5
    const val TRADEIN_BUTTON = 6
    const val TRADEIN_AFTER_DIAGNOSE = 7

    const val REQUEST_CODE_CHECKOUT = 12382

    //OVO
    const val OVO_INACTIVE_STATUS = 1;
    const val OVO_INSUFFICIENT_BALANCE_STATUS = 2;

    const val ENABLE_NEW_DISCUSSION_REMOTE_CONFIG = "android_enable_new_discussion"
    const val SHOW_LABEL_SHARED_PREFERENCE_KEY = "discussion_show_new_%s"
}
