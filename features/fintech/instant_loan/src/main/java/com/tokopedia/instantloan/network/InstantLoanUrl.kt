package com.tokopedia.instantloan.network

import com.tokopedia.instantloan.network.InstantLoanUrl.BaseUrl.WEB_DOMAIN

object InstantLoanUrl {

    object BaseUrl {
        lateinit var WEB_DOMAIN : String
    }

    object COMMON_URL{

        val PATH_USER_STATUS = "dana-instant/api/user/ismobiledevice"
        val PATH_USER_PROFILE_STATUS = WEB_DOMAIN + "dana-instant/api/user/mobile/status"
        val PATH_POST_PHONEDATA = WEB_DOMAIN + "dana-instant/api/user/mobiledevice"
        val PATH_BANNER_OFFER = WEB_DOMAIN + "microfinance/banner/personal-loan"
        val WEB_LINK_NO_COLLATERAL = WEB_DOMAIN + "pinjaman-online/criteria"
        val WEB_LINK_COLLATERAL_FUND = WEB_DOMAIN + "pinjaman-online/criteria"
        val WEB_LINK_DASHBOARD = WEB_DOMAIN + "pinjaman-online/dashboard"
        val WEB_LINK_OTP = WEB_DOMAIN + "pinjaman-online/dana-instant/verify-phone"
        val WEB_LINK_LEARN_MORE = WEB_DOMAIN + "pinjaman-online/profil-kredit/"
        val WEB_LINK_TNC = WEB_DOMAIN + "bantuan/syarat-dan-ketentuan-pinjaman-dana-online/"
        val LOAN_AMOUNT_QUERY_PARAM = "?loan="

        val SUBMISSION_HISTORY_URL = WEB_DOMAIN + "pinjaman-online/dana-instant/dashboard"
        val PAYMENT_METHODS_URL = WEB_DOMAIN + "pinjaman-online/dana-instant/payment-method/%s"
        val HELP_URL = WEB_DOMAIN + "contact-us?pid=162#step1"
    }

}
