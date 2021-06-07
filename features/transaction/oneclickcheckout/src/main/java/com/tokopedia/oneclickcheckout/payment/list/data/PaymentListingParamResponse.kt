package com.tokopedia.oneclickcheckout.payment.list.data

import com.google.gson.annotations.SerializedName

data class PaymentListingParamGqlResponse(
        @SerializedName("getListingParams")
        val response: PaymentListingParam = PaymentListingParam()
)

data class PaymentListingParam(
        @SerializedName("success")
        val success: Boolean = false,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("data")
        val data: ListingParam = ListingParam()
)

data class ListingParam(
        @SerializedName("merchant_code")
        val merchantCode: String = "",
        @SerializedName("profile_code")
        val profileCode: String = "",
        @SerializedName("user_id")
        val userId: String = "",
        @SerializedName("customer_name")
        val customerName: String = "",
        @SerializedName("customer_email")
        val customerEmail: String = "",
        @SerializedName("customer_msisdn")
        val customerMsisdn: String = "",
        @SerializedName("address_id")
        val addressId: String = "",
        @SerializedName("callback_url")
        val callbackUrl: String = "",
        @SerializedName("hash")
        val hash: String = ""
)