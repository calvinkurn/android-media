package com.tokopedia.common_digital.common.constant

import com.tokopedia.url.TokopediaUrl

/**
 * Created by Rizky on 13/08/18.
 */
object DigitalUrl {

    @JvmField
    var DIGITAL_API_DOMAIN = com.tokopedia.url.TokopediaUrl.getInstance().PULSA_API

    val VERSION = "v1.4/"

    var BASE_URL:String? = ""
        get() = DIGITAL_API_DOMAIN + VERSION


}
