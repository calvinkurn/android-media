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
    const val ERROR_TOO_MANY_REQUEST = "TOO_MANY_REQUESTS"
    const val ERROR_INVALID_ITEM_ID = "INVALID_ITEM_ID"
    const val ERROR_INVALID_KEYWORD = "INVALID_KEYWORD_TAG"
    const val EROOR_GROUP_NAME_EXIST = "GRUP_NAME_ALREADY_EXIST"

    /*keyword specific*/
    const val BROAD_TYPE = "Luas"
    const val EXACT_POSITIVE = 21
    const val BROAD_POSITIVE = 11
    const val SPECIFIC_TYPE = "Spesifik"
    const val UNKNOWN_SEARCH = "belum ada data"


    var TOPADS_GRAPHQL_TA_URL = when (TokopediaUrl.getInstance().TYPE) {
        Env.STAGING -> "https://gql-staging.tokopedia.com/graphql/ta"
        else -> "https://gql.tokopedia.com/graphql/ta"
    }
}