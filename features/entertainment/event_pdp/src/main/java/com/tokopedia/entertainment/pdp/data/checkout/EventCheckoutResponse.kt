package com.tokopedia.entertainment.pdp.data.checkout


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventCheckoutResponse(
    @SerializedName("config")
    val config: Any,
    @SerializedName("data")
    val data: DataPayment = DataPayment(),
    @SerializedName("server_process_time")
    val serverProcessTime: String,
    @SerializedName("status")
    val status: String
)

data class DataPayment(
        @SerializedName("amount")
        @Expose
        val amount: Int = 0,
        @SerializedName("currency")
        @Expose
        val currency: String = "",
        @SerializedName("customer_email")
        @Expose
        val customerEmail: String = "",
        @SerializedName("customer_msisdn")
        @Expose
        val customerMsisdn: String = "",
        @SerializedName("customer_name")
        @Expose
        val customerName: String = "",
        @SerializedName("hitPG")
        @Expose
        val hitPG: String = "",
        @SerializedName("items[name]")
        @Expose
        val itemsname: List<String> = emptyList(),
        @SerializedName("items[price]")
        @Expose
        val itemsprice: List<String> = emptyList(),
        @SerializedName("items[quantity]")
        @Expose
        val itemsquantity: List<String> = emptyList(),
        @SerializedName("merchant_code")
        @Expose
        val merchantCode: String = "",
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("payment_metadata")
        @Expose
        val paymentMetadata: String = "",
        @SerializedName("profile_code")
        @Expose
        val profileCode: String = "",
        @SerializedName("refund_amount")
        @Expose
        val refundAmount: Int = 0,
        @SerializedName("signature")
        @Expose
        val signature: String = "",
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("transaction_date")
        @Expose
        val transactionDate: String = "",
        @SerializedName("transaction_id")
        @Expose
        val transactionId: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("user_defined_value")
        @Expose
        val userDefinedValue: String = "",
        @SerializedName("error")
        @Expose
        val error: String = ""

)