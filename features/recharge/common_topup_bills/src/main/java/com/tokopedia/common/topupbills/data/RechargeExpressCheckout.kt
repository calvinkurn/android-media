package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeExpressCheckout {
    @SerializedName("data")
    @Expose
    val data: RechargeExpressCheckoutData = RechargeExpressCheckoutData()

    @SerializedName("errors")
    @Expose
    val errors: List<Error> = listOf()

    class Response {
        @SerializedName("rechargeExpressCheckout")
        @Expose
        val response: RechargeExpressCheckout? = null
    }

    class Error {
        @SerializedName("status")
        @Expose
        val status: String = ""
        @SerializedName("title")
        @Expose
        val title: String = ""
    }
}