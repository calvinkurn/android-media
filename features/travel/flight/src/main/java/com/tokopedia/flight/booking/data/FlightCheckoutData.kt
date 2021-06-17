package com.tokopedia.flight.booking.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-08
 */

data class FlightCheckoutData(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("redirectURL")
        @Expose
        val redirectUrl: String = "",

        @SerializedName("callbackURLSuccess")
        @Expose
        val callbackUrlSuccess: String = "",

        @SerializedName("callbackURLFailed")
        @Expose
        val callbackURLFailed: String = "",

        @SerializedName("queryString")
        @Expose
        val queryString: String = "",

        @SerializedName("parameter")
        @Expose
        val parameter: CheckoutParameter = CheckoutParameter(),

        @SerializedName("thanksURL")
        @Expose
        val thanksURL: String = ""

) {
    data class Response(
            @SerializedName("flightCheckout")
            @Expose
            val flightCheckout: FlightCheckoutData = FlightCheckoutData()
    )

    data class CheckoutParameter(
            @SerializedName("merchantCode")
            @Expose
            val merchantCode: String = "",

            @SerializedName("profileCode")
            @Expose
            val profileCode: String = "",

            @SerializedName("transactionID")
            @Expose
            val transactionId: String = "",

            @SerializedName("transactionCode")
            @Expose
            val transactionCode: String = "",

            @SerializedName("transactionDate")
            @Expose
            val transactionDate: String = "",

            @SerializedName("customerName")
            @Expose
            val customerName: String = "",

            @SerializedName("customerEmail")
            @Expose
            val customerEmail: String = "",

            @SerializedName("amount")
            @Expose
            val amount: String = "",

            @SerializedName("currency")
            @Expose
            val currency: String = "",

            @SerializedName("itemsName")
            @Expose
            val itemsName: List<String> = listOf(),

            @SerializedName("itemsQuantity")
            @Expose
            val itemsQuantity: List<Int> = listOf(),

            @SerializedName("itemsPrice")
            @Expose
            val itemsPrice: List<Int> = listOf(),

            @SerializedName("signature")
            @Expose
            val signature: String = "",

            @SerializedName("language")
            @Expose
            val language: String = "",

            @SerializedName("userDefinedValue")
            @Expose
            val userDefinedValue: String = "",

            @SerializedName("nid")
            @Expose
            val nid: String = "",

            @SerializedName("state")
            @Expose
            val state: Int = 0,

            @SerializedName("fee")
            @Expose
            val fee: String = "",

            @SerializedName("paymentsAmount")
            @Expose
            val paymentsAmount: List<Float> = listOf(),

            @SerializedName("paymentsName")
            @Expose
            val paymentsName: List<String> = listOf(),

            @SerializedName("pid")
            @Expose
            val pid: String = "",

            @SerializedName("customerMsisdn")
            @Expose
            val customerMsisdn: String = ""
    )
}