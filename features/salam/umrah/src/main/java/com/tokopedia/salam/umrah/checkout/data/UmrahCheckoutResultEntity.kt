package com.tokopedia.salam.umrah.checkout.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.intellij.lang.annotations.Language

class UmrahCheckoutResultEntity(
        @SerializedName("checkout_general")
        @Expose
        val checkoutGeneral : UmrahCheckoutGeneral = UmrahCheckoutGeneral()
)

class UmrahCheckoutGeneral(
        @SerializedName("header")
        @Expose
        val header: CheckoutResponseHeader = CheckoutResponseHeader(),
        @SerializedName("data")
        @Expose
        val data: CheckoutResponseData = CheckoutResponseData()
)

class CheckoutResponseHeader(
        @SerializedName("process_time")
        @Expose
        val processTime: Float = 0f,
        @SerializedName("messages")
        @Expose
        val messages: List<String> = emptyList(),
        @SerializedName("reason")
        @Expose
        val reason: String = "",
        @SerializedName("error_code")
        @Expose
        val errorCode: String = ""
)

class CheckoutResponseData(
        @SerializedName("success")
        @Expose
        val success: Int = 0,
        @SerializedName("error")
        @Expose
        val error: String = "",
        @SerializedName("error_state")
        @Expose
        val error_state: Int = 0,
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("data")
        @Expose
        val data: CheckoutResponseDataData= CheckoutResponseDataData()
)


class CheckoutResponseDataData(
        @SerializedName("product_list")
        @Expose
        val productList: List<CheckoutResponseProductList> = emptyList(),
        @SerializedName("redirect_url")
        @Expose
        var redirectUrl: String = "",
        @SerializedName("callback_url")
        @Expose
        var callbackUrl: String = "",
        @SerializedName("query_string")
        @Expose
        var queryString: String = "",
        @SerializedName("payment_type")
        @Expose
        val paymentType: String = "",
        @SerializedName("parameter")
        @Expose
        val parameter: CheckoutResponseParameter = CheckoutResponseParameter()
)

class CheckoutResponseProductList(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("price")
        @Expose
        val price: Float = 0f,
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = ""
)

class CheckoutResponseParameter(
        @SerializedName("merchant_code")
        @Expose
        val merchantCode: String = "",
        @SerializedName("profile_code")
        @Expose
        val profileCode: String = "",
        @SerializedName("customer_id")
        @Expose
        val customerId: Int = 0,
        @SerializedName("customer_name")
        @Expose
        val customerName: String = "",
        @SerializedName("customer_email")
        @Expose
        val customerEmail: String = "",
        @SerializedName("customer_msisdn")
        @Expose
        val customerMsisdn: String = "",
        @SerializedName("transaction_id")
        @Expose
        val transactionId: String = "",
        @SerializedName("transaction_date")
        @Expose
        val transactionDate: String = "",
        @SerializedName("gateway_code")
        @Expose
        val gatewayCode: String = "",
        @SerializedName("pid")
        @Expose
        val pid: String = "",
        @SerializedName("bid")
        @Expose
        val bid: String = "",
        @SerializedName("nid")
        @Expose
        val nid: String = "",
        @SerializedName("user_defined_value")
        @Expose
        val userDefinedValue: String = "",
        @SerializedName("amount")
        @Expose
        val amount: Float = 0f,
        @SerializedName("currency")
        @Expose
        val currency: String = "",
        @SerializedName("language")
        @Expose
        val language: String = "",
        @SerializedName("signature")
        @Expose
        val signature: String = "",
        @SerializedName("back_url")
        @Expose
        val backUrl: String = ""
)

