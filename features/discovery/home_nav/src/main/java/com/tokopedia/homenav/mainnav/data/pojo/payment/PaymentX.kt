package com.tokopedia.homenav.mainnav.data.pojo.payment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentX(
        @SerializedName("gatewayImg")
        @Expose
        val gatewayImg: String? = "",
        @SerializedName("merchantCode")
        @Expose
        val merchantCode: String? = "",
        @SerializedName("paymentAmount")
        @Expose
        val paymentAmount: Int? = 0,
        @SerializedName("tickerMessage")
        @Expose
        val tickerMessage: String? = "",
        @SerializedName("transactionID")
        @Expose
        val transactionID: String? = "",
        @SerializedName("applink")
        @Expose
        val applink: String? = "",
        @SerializedName("bankImg")
        val bankImg: String? = ""
)