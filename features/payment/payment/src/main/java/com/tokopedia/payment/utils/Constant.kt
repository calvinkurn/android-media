package com.tokopedia.payment.utils

/**
 * @author anggaprasetiyo on 10/9/17.
 */
interface Constant {
    @Deprecated("")
    interface TempRedirectPayment {
        companion object {
            const val TOP_PAY_DOMAIN_URL_LIVE = "https://www.tokopedia.com"
            const val TOP_PAY_DOMAIN_URL_STAGING = "https://staging.tokopedia.com"
            const val TOP_PAY_PATH_HELP_URL_TEMPORARY = "/bantuan/sistem-refund-otomatis"
        }
    }
}