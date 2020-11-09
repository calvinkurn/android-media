package com.tokopedia.seamless_login_common.internal

object SeamlessLoginConstant {

    enum class PARAM(val value: String) {
        TOKEN("token"),
        OS_TYPE("os_type"),
        UID("uid"),
        URL("url"),
        VERSION("version"),
        TIMESTAMP("timestamp"),
        BROWSER("browser"),
        DATA("data")
    }

    const val RSA_ALGORITHM = "RSA"
    const val SEAMLESS_PATH = "seamless"

    const val SEAMLESS_KEY = "/seamless"

    const val METHOD_GET = "GET"
    const val HMAC_SIGNATURE = "TKPD Tokopedia:"
    const val BEARER = "Bearer "

    const val BROWSER_VALUE = "1"
    const val OS_TYPE_VALUE = "1"

    const val DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ"
}