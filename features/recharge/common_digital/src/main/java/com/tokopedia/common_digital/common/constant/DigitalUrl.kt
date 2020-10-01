package com.tokopedia.common_digital.common.constant

import com.tokopedia.url.TokopediaUrl

/**
 * Created by Rizky on 13/08/18.
 */
object DigitalUrl {

    @JvmField
    val DIGITAL_API_DOMAIN = TokopediaUrl.getInstance().PULSA_API

    val VERSION = "v1.4/"

    val BASE_URL = DIGITAL_API_DOMAIN + VERSION

    val CART = BASE_URL + "cart"
}
