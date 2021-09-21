package com.tokopedia.moneyin.model


import com.google.gson.annotations.SerializedName

data class MoneyInCheckoutMutationResponse(
    @SerializedName("data")
    val data: ResponseData
) {
    data class ResponseData(
        @SerializedName("checkout_general")
        val checkoutGeneral: CheckoutGeneral
    ) {
        data class CheckoutGeneral(
                @SerializedName("data")
            val data: CheckoutData,
                @SerializedName("header")
            val header: Header
        ) {
            data class Header(
                @SerializedName("error_code")
                val errorCode: String,
                @SerializedName("messages")
                val messages: List<Any>,
                @SerializedName("process_time")
                val processTime: Double,
                @SerializedName("reason")
                val reason: String
            )

            data class CheckoutData(
                    @SerializedName("data")
                val data: Data,
                    @SerializedName("error")
                val error: String,
                    @SerializedName("error_state")
                val errorState: Int,
                    @SerializedName("message")
                val message: String,
                    @SerializedName("success")
                val success: Int
            ) {
                data class Data(
                        @SerializedName("callback_url")
                    val callbackUrl: String,
                        @SerializedName("parameter")
                    val parameter: Parameter,
                        @SerializedName("payment_type")
                    val paymentType: Int,
                        @SerializedName("query_string")
                    val queryString: String,
                        @SerializedName("redirect_url")
                    val redirectUrl: String
                ) {
                    data class Parameter(
                        @SerializedName("amount")
                        val amount: Int,
                        @SerializedName("back_url")
                        val backUrl: String,
                        @SerializedName("currency")
                        val currency: String,
                        @SerializedName("customer_email")
                        val customerEmail: String,
                        @SerializedName("customer_id")
                        val customerId: Int,
                        @SerializedName("customer_msisdn")
                        val customerMsisdn: String,
                        @SerializedName("customer_name")
                        val customerName: String,
                        @SerializedName("gateway_code")
                        val gatewayCode: String,
                        @SerializedName("language")
                        val language: String,
                        @SerializedName("merchant_code")
                        val merchantCode: String,
                        @SerializedName("merchant_type")
                        val merchantType: String,
                        @SerializedName("nid")
                        val nid: String,
                        @SerializedName("payment_metadata")
                        val paymentMetadata: String,
                        @SerializedName("pid")
                        val pid: String,
                        @SerializedName("profile_code")
                        val profileCode: String,
                        @SerializedName("signature")
                        val signature: String,
                        @SerializedName("transaction_date")
                        val transactionDate: String,
                        @SerializedName("transaction_id")
                        val transactionId: String,
                        @SerializedName("user_defined_value")
                        val userDefinedValue: String
                    )
                }
            }
        }
    }
}
