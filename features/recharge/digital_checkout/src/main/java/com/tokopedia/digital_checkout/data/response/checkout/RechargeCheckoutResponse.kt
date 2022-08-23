package com.tokopedia.digital_checkout.data.response.checkout

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 08/03/2022
 */
data class RechargeCheckoutResponse(
    @SerializedName("meta")
    
    val meta: RechargeCheckoutMeta = RechargeCheckoutMeta(),

    @SerializedName("data")
    
    val data: RechargeCheckoutData = RechargeCheckoutData(),

    @SerializedName("errors")
    
    val errors: List<RechargeCheckoutError> = emptyList()
) {
    class Response(
        @SerializedName("rechargeCheckoutV3")
        
        val rechargeCheckoutV3: RechargeCheckoutResponse = RechargeCheckoutResponse()
    )
}

data class RechargeCheckoutData(
    @SerializedName("type")
    
    val type: String = "",
    @SerializedName("id")
    
    val id: String = "",
    @SerializedName("attributes")
    
    val attributes: RechargeCheckoutDataAttribute = RechargeCheckoutDataAttribute()
)

data class RechargeCheckoutDataAttribute(
    @SerializedName("redirect_url")
    
    val redirectUrl: String = "",
    @SerializedName("callback_url_success")
    
    val callbackUrlSuccess: String = "",
    @SerializedName("callback_url_failed")
    
    val callbackUrlFailed: String = "",
    @SerializedName("thanks_url")
    
    val thanksUrl: String = "",
    @SerializedName("query_string")
    
    val queryString: String = "",
    @SerializedName("parameter")
    
    val parameter: RechargeCheckoutAttributeParameter = RechargeCheckoutAttributeParameter()
)

data class RechargeCheckoutAttributeParameter(
    @SerializedName("merchant_code")
    
    val merchantCode: String = "",
    @SerializedName("profile_code")
    
    val profileCode: String = "",
    @SerializedName("transaction_id")
    
    val transactionId: String = "",
    @SerializedName("transaction_code")
    
    val transactionCode: String = "",
    @SerializedName("transaction_date")
    
    val transactionDate: String = "",
    @SerializedName("customer_name")
    
    val customerName: String = "",
    @SerializedName("customer_email")
    
    val customerEmail: String = "",
    @SerializedName("customer_msisdn")
    
    val customerMsisdn: String = "",
    @SerializedName("amount")
    
    val amount: String = "",
    @SerializedName("currency")
    
    val currency: String = "",
    @SerializedName("signature")
    
    val signature: String = "",
    @SerializedName("language")
    
    val language: String = "",
    @SerializedName("user_defined_value")
    
    val userDefinedValue: String = "",
    @SerializedName("nid")
    
    val nid: String = "",
    @SerializedName("state")
    
    val state: Int = 0,
    @SerializedName("fee")
    
    val fee: String = "",
    @SerializedName("pid")
    
    val pid: String = "",
    @SerializedName("items_name")
    
    val itemsName: List<String> = emptyList(),
    @SerializedName("items_quantity")
    
    val itemsQuantity: List<Int> = emptyList(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("items_price")
    
    val itemsPrice: List<Double> = emptyList(),
    @SerializedName("payments_amount")
    
    val paymentsAmount: List<String> = emptyList(),
    @SerializedName("payments_name")
    
    val paymentsName: List<String> = emptyList()
)

data class RechargeCheckoutMeta(
    @SerializedName("order_id")
    
    val orderId: String = ""
)

data class RechargeCheckoutError(
    @SerializedName("status")
    
    val status: String = "",
    @SerializedName("title")
    
    val title: String = ""
)