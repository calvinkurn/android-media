package com.tokopedia.purchase_platform.common.data.api

import com.tokopedia.url.TokopediaUrl

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

open class CommonPurchaseApiUrl {

    companion object {

        const val NET_READ_TIMEOUT = 60
        const val NET_WRITE_TIMEOUT = 60
        const val NET_CONNECT_TIMEOUT = 60
        const val NET_RETRY = 0

        val HMAC_KEY = "web_service_v4"
        var BASE_URL = TokopediaUrl.getInstance().API

        const val BASE_PATH = "cart/"
        const val VERSION = "v2"

        const val PATH_CHECKOUT = "$BASE_PATH$VERSION/checkout"
        const val PATH_SHIPPING_ADDRESS = "$BASE_PATH$VERSION/shipping_address"

    }

}