package com.tokopedia.oneclickcheckout.payment.list.data

data class PaymentListingParamRequest(
        val merchantCode: String,
        val profileCode: String,
        val callbackUrl: String,
        val addressId: String,
        val version: String
)