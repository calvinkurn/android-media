package com.tokopedia.gm.common.constant

import com.tokopedia.url.TokopediaUrl.Companion.getInstance

/**
 * Created by nathan on 10/24/17.
 */
object GMCommonUrl {
    val BASE_URL = getInstance().GOLDMERCHANT
    const val SET_CASHBACK_PRODUCTS = "v1/cashback/set"
    const val FEATURED_PRODUCT_URL = "v1/mobile/featured_product/{shop_id}"
    const val GET_CASHBACK_PRODUCTS = "v1/tx/cashback"
}