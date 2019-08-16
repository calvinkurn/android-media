package com.tokopedia.tradein.model


import com.google.gson.annotations.SerializedName

data class MoneyInCheckoutMutationResponse(
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("checkout")
        val checkout: Checkout?
    ) {
        data class Checkout(
            @SerializedName("data")
            val `data`: Data?,
            @SerializedName("header")
            val header: Header?
        ) {
            data class Header(
                @SerializedName("messages")
                val messages: List<Any?>?
            )

            data class Data(
                @SerializedName("data")
                val `data`: Data?,
                @SerializedName("error")
                val error: String?,
                @SerializedName("message")
                val message: String?,
                @SerializedName("success")
                val success: Int?
            ) {
                data class Data(
                    @SerializedName("callback_url")
                    val callbackUrl: String?,
                    @SerializedName("parameter")
                    val parameter: Parameter?,
                    @SerializedName("product_list")
                    val productList: List<Product?>?,
                    @SerializedName("query_string")
                    val queryString: String?,
                    @SerializedName("redirect_url")
                    val redirectUrl: String?
                ) {
                    data class Parameter(
                        @SerializedName("amount")
                        val amount: Int?,
                        @SerializedName("currency")
                        val currency: String?,
                        @SerializedName("customer_email")
                        val customerEmail: String?,
                        @SerializedName("customer_id")
                        val customerId: Int?,
                        @SerializedName("customer_msisdn")
                        val customerMsisdn: String?,
                        @SerializedName("customer_name")
                        val customerName: String?,
                        @SerializedName("device_info")
                        val deviceInfo: DeviceInfo?,
                        @SerializedName("gateway_code")
                        val gatewayCode: String?,
                        @SerializedName("language")
                        val language: String?,
                        @SerializedName("merchant_code")
                        val merchantCode: String?,
                        @SerializedName("merchant_type")
                        val merchantType: String?,
                        @SerializedName("nid")
                        val nid: String?,
                        @SerializedName("payment_metadata")
                        val paymentMetadata: String?,
                        @SerializedName("pid")
                        val pid: String?,
                        @SerializedName("profile_code")
                        val profileCode: String?,
                        @SerializedName("signature")
                        val signature: String?,
                        @SerializedName("transaction_date")
                        val transactionDate: String?,
                        @SerializedName("transaction_id")
                        val transactionId: String?,
                        @SerializedName("user_defined_value")
                        val userDefinedValue: String?
                    ) {
                        data class DeviceInfo(
                            @SerializedName("device_name")
                            val deviceName: String?,
                            @SerializedName("device_version")
                            val deviceVersion: String?
                        )
                    }

                    data class Product(
                        @SerializedName("id")
                        val id: String?,
                        @SerializedName("name")
                        val name: String?,
                        @SerializedName("price")
                        val price: Int?,
                        @SerializedName("quantity")
                        val quantity: Int?
                    )
                }
            }
        }
    }
}