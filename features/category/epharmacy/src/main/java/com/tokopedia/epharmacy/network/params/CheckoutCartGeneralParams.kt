package com.tokopedia.epharmacy.network.params

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CheckoutCartGeneralParams(
    @SerializedName("transaction")
    @Expose
    val transaction: Transaction
) {
    data class Transaction(
        @SerializedName("business_data")
        @Expose
        val businessData: List<BusinessData?>?,
        @SerializedName("flow_type")
        @Expose
        val flowType: String?
    ) {
        data class BusinessData(
            @SerializedName("business_id")
            @Expose
            val businessId: String?,
            @SerializedName("cart_groups")
            @Expose
            val cartGroups: List<CartGroup?>?,
            @SerializedName("checkout_business_type")
            @Expose
            val checkoutBusinessType: Int?,
            @SerializedName("checkout_data_type")
            @Expose
            val checkoutDataType: String?
        ) {
            data class CartGroup(
                @SerializedName("cart_group_id")
                @Expose
                val cartGroupId: String?,
                @SerializedName("carts")
                @Expose
                val carts: List<Cart?>?
            ) {
                data class Cart(
                    @SerializedName("cart_id")
                    @Expose
                    val cartId: String?,
                    @SerializedName("metadata")
                    @Expose
                    val metadata: Metadata?,
                    @SerializedName("price")
                    @Expose
                    val price: Double?,
                    @SerializedName("product_id")
                    @Expose
                    val productId: String?,
                    @SerializedName("quantity")
                    @Expose
                    val quantity: Int?,
                    @SerializedName("shop_id")
                    @Expose
                    val shopId: String?
                ) {
                    data class Metadata(
                        @SerializedName("enabler_id")
                        @Expose
                        val enablerId: Long?,
                        @SerializedName("epharmacy_group_id")
                        @Expose
                        val epharmacyGroupId: String?,
                        @SerializedName("toko_consultation_id")
                        @Expose
                        val tokoConsultationId: Long?
                    )
                }
            }
        }
    }
}
