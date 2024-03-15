package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EPharmacyAtcInstantResponse(
    @SerializedName("cart_general_add_to_cart_instant")
    @Expose
    val cartGeneralAddToCartInstant: CartGeneralAddToCartInstant?
) {
    data class CartGeneralAddToCartInstant(
        @SerializedName("data")
        @Expose
        val cartGeneralAddToCartInstantData: CartGeneralAddToCartInstantData?
    ) {
        data class CartGeneralAddToCartInstantData(
            @SerializedName("data")
            @Expose
            val businessDataList: BusinessDataList?,
            @SerializedName("message")
            @Expose
            val message: String?,
            @SerializedName("success")
            @Expose
            val success: Int?
        ) {
            data class BusinessDataList(
                @SerializedName("business_data")
                @Expose
                val businessData: List<BusinessData?>?,
                @SerializedName("shopping_summary")
                @Expose
                val shoppingSummary: BusinessData.ShoppingSummary?
            ) {
                data class BusinessData(
                    @SerializedName("business_id")
                    @Expose
                    val businessId: String?,
                    @SerializedName("cart_groups")
                    @Expose
                    val cartGroups: List<CartGroup?>?,
                    @SerializedName("custom_response")
                    @Expose
                    val customResponse: CustomResponse?,
                    @SerializedName("message")
                    @Expose
                    val message: String?,
                    @SerializedName("success")
                    @Expose
                    val success: Int?
                ) {
                    data class CartGroup(
                        @SerializedName("cart_group_id")
                        @Expose
                        val cartGroupId: String?,
                        @SerializedName("carts")
                        @Expose
                        val carts: List<Cart?>?,
                        @SerializedName("success")
                        @Expose
                        val success: Int?
                    ) {
                        data class Cart(
                            @SerializedName("cart_id")
                            @Expose
                            val cartId: String?,
                            @SerializedName("custom_response")
                            @Expose
                            val customResponse: CustomResponse?,
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
                            val shopId: String?,
                            @SerializedName("success")
                            @Expose
                            val success: Int?
                        ) {
                            data class CustomResponse(
                                @SerializedName("duration_minutes")
                                @Expose
                                val durationMinutes: String?,
                                @SerializedName("enabler_name")
                                @Expose
                                val enablerName: String?,
                                @SerializedName("service_type")
                                @Expose
                                val serviceType: String?
                            )

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

                    data class CustomResponse(
                        @SerializedName("checkout_additional_data")
                        @Expose
                        val checkoutAdditionalData: CheckoutAdditionalData?,
                        @SerializedName("img_url")
                        @Expose
                        val imgUrl: String?,
                        @SerializedName("title")
                        @Expose
                        val title: String?
                    ) {
                        data class CheckoutAdditionalData(
                            @SerializedName("checkout_business_id")
                            @Expose
                            val checkoutBusinessId: String?,
                            @SerializedName("data_type")
                            @Expose
                            val dataType: String?,
                            @SerializedName("flow_type")
                            @Expose
                            val flowType: String?
                        )
                    }

                    data class ShoppingSummary(
                        @SerializedName("business_breakdown")
                        @Expose
                        val businessBreakDown: List<BusinessBreakDown>?,
                    ) {
                        data class BusinessBreakDown(
                            @SerializedName("product")
                            @Expose
                            val product: Product?,
                            @SerializedName("total_bill_fmt")
                            @Expose
                            val totalBillFmt: String?
                        )

                        data class Product(
                            @SerializedName("title")
                            @Expose
                            val title: String?,
                            @SerializedName("total_cart")
                            @Expose
                            val totalCart: Int?,
                            @SerializedName("total_price")
                            @Expose
                            val totalPrice: String?,
                            @SerializedName("total_price_fmt")
                            @Expose
                            val totalPriceFmt: String?,
                            @SerializedName("total_quantity")
                            @Expose
                            val totalQuantity: Int?
                        )
                    }
                }
            }
        }
    }
}
