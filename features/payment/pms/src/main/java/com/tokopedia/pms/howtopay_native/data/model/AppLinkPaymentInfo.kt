package com.tokopedia.pms.howtopay_native.data.model

import com.google.gson.annotations.SerializedName

data class AppLinkPaymentInfo(
        @SerializedName("merchant_code") var merchantCode : String?,
        @SerializedName("transaction_id") val transactionId : String
)