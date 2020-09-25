package com.tokopedia.oneclickcheckout.preference.edit.data.payment

data class PaymentListingParamRequest(
        val merchantCode: String,
        val profileCode: String,
        val callbackUrl: String,
        val addressId: String,
        val version: String
)