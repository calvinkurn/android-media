package com.tokopedia.checkoutpayment

import com.tokopedia.config.GlobalConfig

internal fun generateAppVersionForPayment(): String {
    return "android-${GlobalConfig.VERSION_NAME}"
}
