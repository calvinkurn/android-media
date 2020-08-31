package com.tokopedia.payment.utils

/**
 * @author anggaprasetiyo on 10/9/17.
 */
interface Constant {
    @Deprecated("")
    interface TempRedirectPayment {
        companion object {
            private const val TOP_PAY_DOMAIN_URL_LIVE = "https://www.tokopedia.com"
            private const val TOP_PAY_DOMAIN_URL_STAGING = "https://staging.tokopedia.com"
            private const val TOP_PAY_PATH_HELP_URL_TEMPORARY = "/bantuan/sistem-refund-otomatis"

            val TOP_PAY_LIVE_HELP_URL = TOP_PAY_DOMAIN_URL_LIVE + TOP_PAY_PATH_HELP_URL_TEMPORARY
            val TOP_PAY_STAGING_HELP_URL = TOP_PAY_DOMAIN_URL_STAGING + TOP_PAY_PATH_HELP_URL_TEMPORARY
        }
    }
}

val HEADER_TKPD_USER_AGENT = "tkpd-useragent"
val HEADER_TKPD_SESSION_ID = "tkpd-sessionid"