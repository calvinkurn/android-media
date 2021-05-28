package com.tokopedia.topads

import com.tokopedia.url.TokopediaUrl

/**
 * Author errysuprayogi on 08,November,2019
 */
object UrlConstant {

    @JvmField
    var BASE_REST_URL = TokopediaUrl.getInstance().TA

    const val PATH_PRODUCT_LIST = "v2.2/dashboard/search_products"
    const val PATH_GROUP_CREATE = "v2.2/promo/group"
    const val PATH_GROUP_VALIDATE = "v2.2/promo/group/validate"
    const val PATH_KEYWORD_CREATE = "v2.1/promo/keyword"
    const val MAIN = "Main"

    const val fragment_number_1 = 1
    const val fragment_number_2 = 2
    const val fragment_number_3 = 3
    const val fragment_number_4 = 4
}