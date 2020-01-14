package com.tokopedia.topads.detail_sheet

import com.tokopedia.url.TokopediaUrl

/**
 * Author errysuprayogi on 23,October,2019
 */

object UrlConstant {

    @JvmField
    var BASE_REST_URL = TokopediaUrl.getInstance().TA

    const val PATH_TOPADS_GROUP_PRODUCT = "v1.1/dashboard/group_products"
    const val PATH_BULK_ACTION_PRODUCT_AD = "v2.1/promo/bulk"

    const val PARAM_AD_ID = "ad_id"
    const val PARAM_SHOP_ID = "shop_id"
    const val PARAM_START_DATE = "start_date"
    const val PARAM_END_DATE = "end_date"
}