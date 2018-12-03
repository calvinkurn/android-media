package com.tokopedia.flashsale.management.data

import com.tokopedia.flashsale.management.R

object FlashSaleConstant {
    const val KEY_STATUS_REGISTRATION = "pendaftaran"
    const val KEY_STATUS_CANCELLED = "dibatalkan"
    const val KEY_STATUS_PRODUCT_SELECTION = "seleksi produk"
    const val KEY_STATUS_SELECTION_ENDED = "seleksi selesai"
    const val KEY_STATUS_ACTIVE = "aktif"
    const val KEY_STATUS_ENDED = "berakhir"

    const val PARAM_ALL = "all"
    const val PARAM_OFFSET = "offset"
    const val PARAM_SOURCE = "source"
    const val PARAM_START = "start"
    const val PARAM_ROWS = "rows"
    const val PARAM_SS = "ss" // system status
    const val PARAM_MAS = "mas" // mass admin status
    const val PARAM_SLUG = "slug"
    const val PARAM_SHOP_ID = "shop_id"
    const val PARAM_CAMPAIGN_TYPE = "campaignType"
    const val PARAM_CAMP_ID = "camp_id"
    const val PARAM_CID = "cid"
    const val PARAM_CAMPAIGN_ID = "campaign_id"
    const val PARAM_CRITERIA_ID = "criteria_id"
    const val PARAM_PRODUCT_ID = "product_id"
    const val PARAM_DISCOUNTED_PRICE = "discounted_price"
    const val PARAM_CUSTOM_STOCK = "custom_stock"
    const val PARAM_CASHBACK = "cashback"
    const val PARAM_Q = "q"
    const val PARAM_FILTER = "filter"
    const val PARAM_STATUS = "status"

    //DO NOT CHANGE System Status value. It is from API
    const val SYSTEM_STATUS_ACCEPTED = 1
    // currently below system value is not used
    /*const val SYSTEM_STATUS_REJECTED = 2
    const val SYSTEM_STATUS_RESERVED = 3
    const val SYSTEM_STATUS_SUBMIT_CANCELLED = 4
    const val SYSTEM_STATUS_SELLER_REJECTED = 5*/

    const val SOURCE_SELLERAPP = "sellerapp"

    const val NAMED_REQUEST_SUBMISSION_PRODUCT_LIST = "request_product_list"
    const val NAMED_REQUEST_POST_PRODUCT_LIST = "request_post_product_list"
    const val NAMED_REQUEST_CATEGORY_LIST = "request_category_list"
    const val NAMED_REQUEST_TNC = "request_tnc"
    const val NAMED_REQUEST_SUBMIT_PRODUCT = "request_submit_product"
    const val NAMED_REQUEST_RESERVE_PRODUCT = "request_reserve_product"
    const val NAMED_REQUEST_DERESERVE_PRODUCT = "request_dereserve_product"

    const val NAMED_REQUEST_CAMPAIGN_LABEL = "request_campaign_label"
    const val NAMED_REQUEST_CAMPAIGN_LIST = "request_campaign_list"
    const val NAMED_REQUEST_CAMPAIGN = "request_campaign"
    const val NAMED_REQUEST_SELLER_STATUS = "request_seller_status"

    const val FLASHSALE_ABOUT_URL = "https://www.tokopedia.com/flash-sale/about/"

    val statusColorList = mapOf(
            KEY_STATUS_REGISTRATION to Pair(R.color.status_red, R.color.red_50),
            KEY_STATUS_CANCELLED to Pair(R.color.status_red, R.color.red_50),
            KEY_STATUS_PRODUCT_SELECTION to Pair(R.color.status_blue, R.color.status_blue_bg),
            KEY_STATUS_SELECTION_ENDED to Pair(R.color.bg_corner_yellow, R.color.status_yellow_bg),
            KEY_STATUS_ACTIVE to Pair(R.color.tkpd_main_green, R.color.green_active),
            KEY_STATUS_ENDED to Pair(R.color.font_black_disabled_38, R.color.grey_300)
    )

    val statusStepImages = mapOf(
            KEY_STATUS_REGISTRATION to R.drawable.ic_fs_step_registration,
            KEY_STATUS_CANCELLED to R.drawable.ic_fs_step_cancelled,
            KEY_STATUS_PRODUCT_SELECTION to R.drawable.ic_fs_step_product_selection,
            KEY_STATUS_SELECTION_ENDED to R.drawable.ic_fs_step_selection_ended,
            KEY_STATUS_ACTIVE to R.drawable.ic_fs_step_active,
            KEY_STATUS_ENDED to R.drawable.ic_fs_step_ended
    )

    val defaultPairColor = R.color.font_black_disabled_38 to R.color.grey_300
}