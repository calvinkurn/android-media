package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EPharmacyCartGeneralCheckoutResponse(
    @SerializedName("checkout_cart_general")
    @Expose
    val checkout: CheckoutResponse?
) {

    companion object {
        const val SUCCESS = 1
        const val ERROR = 0
    }

    data class CheckoutResponse(
        @SerializedName("data")
        @Expose
        val checkoutData: CheckoutData?
    ) {
        data class CheckoutData(
            @SerializedName("data")
            @Expose
            val cartGeneralResponse: CartGeneralResponse?,
            @SerializedName("error")
            @Expose
            val error: String?,
            @SerializedName("message")
            @Expose
            val message: String?,
            @SerializedName("success")
            @Expose
            val success: Int?
        ) {
            data class CartGeneralResponse(
                @SerializedName("callback_url")
                @Expose
                val callbackUrl: String?,
                @SerializedName("callback_fail_url")
                @Expose
                val callbackFailUrl: String?,
                @SerializedName("parameter")
                @Expose
                val parameter: Parameter?,
                @SerializedName("product_list")
                @Expose
                val productList: List<Product?>?,
                @SerializedName("query_string")
                @Expose
                val queryString: String?,
                @SerializedName("redirect_url")
                @Expose
                val redirectUrl: String?
            ) {
                data class Parameter(
                    @SerializedName("amount")
                    @Expose
                    val amount: Int?,
                    @SerializedName("currency")
                    @Expose
                    val currency: String?,
                    @SerializedName("customer_email")
                    @Expose
                    val customerEmail: String?,
                    @SerializedName("customer_id")
                    @Expose
                    val customerId: String?,
                    @SerializedName("customer_msisdn")
                    @Expose
                    val customerMsisdn: String?,
                    @SerializedName("customer_name")
                    @Expose
                    val customerName: String?,
                    @SerializedName("device_info")
                    @Expose
                    val deviceInfo: DeviceInfo?,
                    @SerializedName("gateway_code")
                    @Expose
                    val gatewayCode: String?,
                    @SerializedName("language")
                    @Expose
                    val language: String?,
                    @SerializedName("merchant_code")
                    @Expose
                    val merchantCode: String?,
                    @SerializedName("merchant_type")
                    @Expose
                    val merchantType: String?,
                    @SerializedName("nid")
                    @Expose
                    val nid: String?,
                    @SerializedName("payment_metadata")
                    @Expose
                    val paymentMetadata: String?,
                    @SerializedName("pid")
                    @Expose
                    val pid: String?,
                    @SerializedName("profile_code")
                    @Expose
                    val profileCode: String?,
                    @SerializedName("signature")
                    @Expose
                    val signature: String?,
                    @SerializedName("transaction_date")
                    @Expose
                    val transactionDate: String?,
                    @SerializedName("transaction_id")
                    @Expose
                    val transactionId: String?,
                    @SerializedName("user_defined_value")
                    @Expose
                    val userDefinedValue: String?
                ) {
                    data class DeviceInfo(
                        @SerializedName("device_name")
                        @Expose
                        val deviceName: String?,
                        @SerializedName("device_version")
                        @Expose
                        val deviceVersion: String?
                    )
                }

                data class Product(
                    @SerializedName("id")
                    @Expose
                    val id: String?,
                    @SerializedName("name")
                    @Expose
                    val name: String?,
                    @SerializedName("price")
                    @Expose
                    val price: Double?,
                    @SerializedName("quantity")
                    @Expose
                    val quantity: Int?
                )
            }
        }
    }
}
