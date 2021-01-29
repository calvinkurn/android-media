package com.tokopedia.digital_checkout.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier

data class RequestBodyCheckout(

        @SerializedName("type")
        @Expose
        var type: String? = null,

        @SerializedName("attributes")
        @Expose
        var attributes: AttributesCheckout? = null,

        @SerializedName("relationships")
        @Expose
        var relationships: CheckoutRelationships? = null
) {
    class AttributesCheckout(

            @SerializedName("voucher_code")
            @Expose
            var voucherCode: String? = null,

            @SerializedName("transaction_amount")
            @Expose
            var transactionAmount: Long? = null,

            @SerializedName("ip_address")
            @Expose
            var ipAddress: String? = null,

            @SerializedName("user_agent")
            @Expose
            var userAgent: String? = null,

            @SerializedName("identifier")
            @Expose
            var identifier: RequestBodyIdentifier? = null,

            @SerializedName("appsflyer")
            @Expose
            var appsFlyer: RequestBodyAppsFlyer? = null,

            @SerializedName("client_id")
            @Expose
            var clientId: String? = null,

            @SerializedName("subscribe")
            @Expose
            var subscribe: Boolean? = false,

            @SerializedName("deals_ids")
            @Expose
            var dealsIds: List<Int>? = null,

            @SerializedName("fintech_product")
            @Expose
            var fintechProduct: List<FintechProductCheckout>? = null
    )

    data class RequestBodyAppsFlyer(
            @SerializedName("appsflyer_id")
            @Expose
            var appsflyerId: String? = null,

            @SerializedName("device_id")
            @Expose
            var deviceId: String? = null
    )


    data class FintechProductCheckout(
            @SerializedName("transaction_type")
            @Expose
            var transactionType: String? = null,
            @SerializedName("tier_id")
            @Expose
            var tierId: Int = 0,
            @SerializedName("user_id")
            @Expose
            var userId: Long? = null,
            @SerializedName("fintech_amount")
            @Expose
            var fintechAmount: Long = 0,
            @SerializedName("fintech_partner_amount")
            @Expose
            var fintechPartnerAmount: Long = 0,
            @SerializedName("product_name")
            @Expose
            var productName: String? = null
    )
}
