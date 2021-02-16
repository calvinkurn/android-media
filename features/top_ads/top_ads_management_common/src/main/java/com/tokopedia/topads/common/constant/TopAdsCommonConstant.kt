package com.tokopedia.topads.common.constant

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

/**
 * Created by hadi.putra on 23/04/18.
 */
object TopAdsCommonConstant {
    @JvmField
    var BASE_DOMAIN_URL = TokopediaUrl.getInstance().TA
    const val TOPADS_SELLER_CENTER = "https://seller.tokopedia.com/about-topads/"
    const val PARAM_SHOP_ID = "shop_id"
    const val DIRECTED_FROM_MANAGE_OR_PDP = "directed_from_manage_or_pdp"
    const val REQUEST_DATE_FORMAT = "yyyy-MM-dd"
    const val ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val TOPADS_MOVE_TO_DASHBOARD = "move_to_dashboard"
    const val TOPADS_AUTOADS_BUDGET_UPDATED = "budget_updated"
    const val PARAM_PRODUK_IKLAN = 1
    const val PARAM_AUTOADS_BUDGET = 1
    var TOPADS_GRAPHQL_TA_URL = when (TokopediaUrl.getInstance().TYPE) {
        Env.STAGING -> "https://gql-staging.tokopedia.com/graphql/ta"
        else -> "https://gql.tokopedia.com/graphql/ta"
    }
}