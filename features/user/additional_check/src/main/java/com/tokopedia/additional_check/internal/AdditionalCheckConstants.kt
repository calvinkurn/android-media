package com.tokopedia.additional_check.internal

object AdditionalCheckConstants {

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

    const val QUERY_CHECK_BOTTOM_SHEET = "showInterrupt"

    const val REMOTE_CONFIG_2FA = "android_user_two_factor_check"
    const val REMOTE_CONFIG_2FA_SELLER_APP = "android_user_two_factor_check_seller_app"
    const val REMOTE_CONFIG_DOUBLE_TAP = "android_user_two_factor_double_tap"

    const val POPUP_TYPE_NONE = 0
    const val POPUP_TYPE_PIN = 1
    const val POPUP_TYPE_PHONE = 2
    const val POPUP_TYPE_BOTH = 3

}