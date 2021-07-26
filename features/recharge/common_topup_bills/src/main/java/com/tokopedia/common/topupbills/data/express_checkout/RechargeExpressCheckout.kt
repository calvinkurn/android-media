package com.tokopedia.common.topupbills.data.express_checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeExpressCheckout (
    @SerializedName("data")
    @Expose
    val data: RechargeExpressCheckoutData = RechargeExpressCheckoutData(),

    @SerializedName("errors")
    @Expose
    val errors: List<Error> = listOf()
) {
    data class Response (
        @SerializedName("rechargeExpressCheckout")
        @Expose
        val response: RechargeExpressCheckout? = null
    )

    data class Error (
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("title")
        @Expose
        val title: String = ""
    )
}