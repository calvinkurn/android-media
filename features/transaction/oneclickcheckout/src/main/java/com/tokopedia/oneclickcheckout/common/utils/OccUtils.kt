package com.tokopedia.oneclickcheckout.common.utils

import com.tokopedia.config.GlobalConfig

internal fun generateAppVersionForPayment(): String {
    return "android-${GlobalConfig.VERSION_NAME}"
}
