package com.tokopedia.payment.setting.util

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

var PAYMENT_SETTING_URL = when (TokopediaUrl.getInstance().TYPE) {
    Env.STAGING -> "https://pay-staging.tokopedia.id/"
    else -> "https://pay.tokopedia.id/"
}

const val GET_IFRAME_ADD_CC_URL = "v2/ccregister/iframe"
const val PAYMENT_IMAGE_HOST = "https://images.tokopedia.net/img/toppay/"
