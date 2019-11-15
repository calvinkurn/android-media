package com.tokopedia.seamless_login.internal

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
    const val SEAMLESS_PATH = "/seamless"

    const val METHOD_GET = "GET"
    const val HMAC_SIGNATURE = "TKPD Tokopedia:"
    const val BEARER = "Bearer "

}