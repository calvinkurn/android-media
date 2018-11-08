package com.tokopedia.flashsale.management.data

import com.tokopedia.flashsale.management.R

object FlashSaleConstant {
    const val PARAM_ALL = "all"
    const val PARAM_OFFSET = "offset"
    const val PARAM_ROWS = "rows"
    const val PARAM_SLUG = "slug"
    const val PARAM_SHOP_ID = "shop_id"
    const val PARAM_CAMPAIGN_TYPE = "campaignType"
    const val PARAM_CAMP_ID = "camp_id"
    const val PARAM_QUERY = "q"
    const val PARAM_FILTER = "filter"
    const val PARAM_STATUS = "status"

    const val NAMED_REQUEST_PRODUCT_LIST = "request_product_list"
    const val NAMED_REQUEST_CATEGORY_LIST = "request_category_list"
    const val NAMED_REQUEST_TNC = "request_tnc"
    const val NAMED_REQUEST_SUBMIT_PRODUCT = "request_submit_product"

    const val NAMED_REQUEST_CAMPAIGN_LABEL = "request_campaign_label"
    const val NAMED_REQUEST_CAMPAIGN_LIST = "request_campaign_list"
    const val NAMED_REQUEST_CAMPAIGN = "request_campaign"
    const val NAMED_REQUEST_SELLER_STATUS = "request_seller_status"

    val statusColorList = mapOf(
            "pendaftaran" to Pair(R.color.status_red, R.color.red_50),
            "dibatalkan" to Pair(R.color.status_red, R.color.red_50),
            "seleksi produk" to Pair(R.color.status_blue, R.color.status_blue_bg),
            "seleksi selesai" to Pair(R.color.bg_corner_yellow, R.color.status_yellow_bg),
            "aktif" to Pair(R.color.tkpd_main_green, R.color.green_50),
            "berakhir" to Pair(R.color.font_black_disabled_38, R.color.grey_300)
    )

    val defaultPairColor = R.color.font_black_disabled_38 to R.color.grey_300
}