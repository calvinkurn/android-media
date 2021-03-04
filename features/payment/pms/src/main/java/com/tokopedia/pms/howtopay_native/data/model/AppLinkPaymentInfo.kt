package com.tokopedia.pms.howtopay_native.data.model

import com.google.gson.annotations.SerializedName

data class AppLinkPaymentInfo(
        @SerializedName("bank_code") var bank_code : String?,
        @SerializedName("bank_info") val bank_info : String,
        @SerializedName("bank_num") val bank_num : String,
        @SerializedName("bank_name") val bank_name : String,
        @SerializedName("bank_logo") val bank_logo : String?,
        @SerializedName("gateway_code") val gateway_code : String,
        @SerializedName("gateway_logo") val gateway_logo : String?,
        @SerializedName("gateway_name") val gateway_name : String,
        @SerializedName("payment_code") val payment_code : String,
        @SerializedName("payment_type") val payment_type : String,
        @SerializedName("total_amount") val total_amount : String,
        @SerializedName("deadline") val deadline : String,
        @SerializedName("transaction_id") val transaction_id : String
)