package com.tokopedia.checkout.data.api

import com.tokopedia.url.TokopediaUrl

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

open class CommonPurchaseApiUrl {

    companion object {

        val HMAC_KEY = "web_service_v4"

        const val BASE_PATH = "cart/"
        const val VERSION = "v2"

        const val PATH_CHECKOUT = "$BASE_PATH$VERSION/checkout"

    }

}