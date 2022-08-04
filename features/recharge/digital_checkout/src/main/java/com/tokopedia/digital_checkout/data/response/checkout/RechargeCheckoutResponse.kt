package com.tokopedia.digital_checkout.data.response.checkout

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 08/03/2022
 */
data class RechargeCheckoutResponse(
    @SerializedName("meta")
    @Expose
    val meta: RechargeCheckoutMeta = RechargeCheckoutMeta(),

    @SerializedName("data")
    @Expose
    val data: RechargeCheckoutData = RechargeCheckoutData(),

    @SerializedName("errors")
    @Expose
    val errors: List<RechargeCheckoutError> = emptyList()
) {
    class Response(
        @SerializedName("rechargeCheckoutV3")
        @Expose
        val rechargeCheckoutV3: RechargeCheckoutResponse = RechargeCheckoutResponse()
    )
}

data class RechargeCheckoutData(
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("attributes")
    @Expose
    val attributes: RechargeCheckoutDataAttribute = RechargeCheckoutDataAttribute()
)

data class RechargeCheckoutDataAttribute(
    @SerializedName("redirect_url")
    @Expose
    val redirectUrl: String = "",
    @SerializedName("callback_url_success")
    @Expose
    val callbackUrlSuccess: String = "",
    @SerializedName("callback_url_failed")
    @Expose
    val callbackUrlFailed: String = "",
    @SerializedName("thanks_url")
    @Expose
    val thanksUrl: String = "",
    @SerializedName("query_string")
    @Expose
    val queryString: String = "",
    @SerializedName("parameter")
    @Expose
    val parameter: RechargeCheckoutAttributeParameter = RechargeCheckoutAttributeParameter()
)

data class RechargeCheckoutAttributeParameter(
    @SerializedName("merchant_code")
    @Expose
    val merchantCode: String = "",
    @SerializedName("profile_code")
    @Expose
    val profileCode: String = "",
    @SerializedName("transaction_id")
    @Expose
    val transactionId: String = "",
    @SerializedName("transaction_code")
    @Expose
    val transactionCode: String = "",
    @SerializedName("transaction_date")
    @Expose
    val transactionDate: String = "",
    @SerializedName("customer_name")
    @Expose
    val customerName: String = "",
    @SerializedName("customer_email")
    @Expose
    val customerEmail: String = "",
    @SerializedName("customer_msisdn")
    @Expose
    val customerMsisdn: String = "",
    @SerializedName("amount")
    @Expose
    val amount: String = "",
    @SerializedName("currency")
    @Expose
    val currency: String = "",
    @SerializedName("signature")
    @Expose
    val signature: String = "",
    @SerializedName("language")
    @Expose
    val language: String = "",
    @SerializedName("user_defined_value")
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
    @SerializedName("pid")
    @Expose
    val pid: String = "",
    @SerializedName("items_name")
    @Expose
    val itemsName: List<String> = emptyList(),
    @SerializedName("items_quantity")
    @Expose
    val itemsQuantity: List<Int> = emptyList(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("items_price")
    @Expose
    val itemsPrice: List<Double> = emptyList(),
    @SerializedName("payments_amount")
    @Expose
    val paymentsAmount: List<String> = emptyList(),
    @SerializedName("payments_name")
    @Expose
    val paymentsName: List<String> = emptyList()
)

data class RechargeCheckoutMeta(
    @SerializedName("order_id")
    @Expose
    val orderId: String = ""
)

data class RechargeCheckoutError(
    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("title")
    @Expose
    val title: String = ""
)