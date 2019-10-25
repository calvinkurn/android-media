package com.tokopedia.topads.widget.dashboard.internal

import com.tokopedia.url.TokopediaUrl

/**
 * Author errysuprayogi on 25,October,2019
 */
object UrlConstant {

    @JvmField
    var BASE_REST_URL = TokopediaUrl.getInstance().TA

    const val PATH_TOPADS_DASHBOARD_STATISTIC = "v1.1/dashboard/group_products"
}
