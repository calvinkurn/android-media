package com.tokopedia.digital_checkout.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier

data class RequestBodyCheckout(

        @SerializedName("type")
        @Expose
        var type: String = "",

        @SerializedName("attributes")
        @Expose
        var attributes: AttributesCheckout = AttributesCheckout(),

        @SerializedName("relationships")
        @Expose
        var relationships: CheckoutRelationships = CheckoutRelationships()
) {
    class AttributesCheckout(

            @SerializedName("voucher_code")
            @Expose
            var voucherCode: String = "",

            @SerializedName("transaction_amount")
            @Expose
            var transactionAmount: Long = 0,

            @SerializedName("ip_address")
            @Expose
            var ipAddress: String = "",

            @SerializedName("user_agent")
            @Expose
            var userAgent: String = "",

            @SerializedName("identifier")
            @Expose
            var identifier: RequestBodyIdentifier = RequestBodyIdentifier(),

            @SerializedName("appsflyer")
            @Expose
            var appsFlyer: RequestBodyAppsFlyer = RequestBodyAppsFlyer(),

            @SerializedName("client_id")
            @Expose
            var clientId: String = "",

            @SerializedName("subscribe")
            @Expose
            var subscribe: Boolean = false,

            @SerializedName("deals_ids")
            @Expose
            var dealsIds: List<Int> = listOf(),

            @SerializedName("fintech_product")
            @Expose
            var fintechProduct: List<FintechProductCheckout> = listOf()
    )

    data class RequestBodyAppsFlyer(
            @SerializedName("appsflyer_id")
            @Expose
            var appsflyerId: String = "",

            @SerializedName("device_id")
            @Expose
            var deviceId: String = ""
    )


    data class FintechProductCheckout(
            @SerializedName("transaction_type")
            @Expose
            var transactionType: String = "",
            @SerializedName("tier_id")
            @Expose
            var tierId: Int = 0,
            @SerializedName("user_id")
            @Expose
            var userId: Long = 0,
            @SerializedName("fintech_amount")
            @Expose
            var fintechAmount: Long = 0,
            @SerializedName("fintech_partner_amount")
            @Expose
            var fintechPartnerAmount: Long = 0,
            @SerializedName("product_name")
            @Expose
            var productName: String = ""
    )
}
