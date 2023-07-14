package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EPharmacyCheckoutResponse(
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
                val businessData: List<BusinessData?>?
            ) {
                data class BusinessData(
                    @SerializedName("additional_grouping")
                    @Expose
                    val additionalGrouping: AdditionalGrouping?,
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
                    @SerializedName("shopping_summary")
                    @Expose
                    val shoppingSummary: ShoppingSummary?,
                    @SerializedName("success")
                    @Expose
                    val success: Int?,
                    @SerializedName("ticker")
                    @Expose
                    val ticker: Ticker?
                ) {
                    class AdditionalGrouping

                    data class CartGroup(
                        @SerializedName("cart_group_id")
                        @Expose
                        val cartGroupId: String?,
                        @SerializedName("carts")
                        @Expose
                        val carts: List<Cart?>?,
                        @SerializedName("custom_response")
                        @Expose
                        val customResponse: CustomResponse?,
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
                            val price: Int?,
                            @SerializedName("price_fmt")
                            @Expose
                            val priceFmt: String?,
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
                                val durationMinutes: Int?,
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
                                val enablerId: Int?,
                                @SerializedName("epharmacy_group_id")
                                @Expose
                                val epharmacyGroupId: String?,
                                @SerializedName("toko_consultation_id")
                                @Expose
                                val tokoConsultationId: Int?
                            )
                        }

                        class CustomResponse
                    }

                    data class CustomResponse(
                        @SerializedName("img_icon")
                        @Expose
                        val imgIcon: String?,
                        @SerializedName("title")
                        @Expose
                        val title: String?
                    )

                    data class ShoppingSummary(
                        @SerializedName("add_ons")
                        @Expose
                        val addOns: List<Any?>?,
                        @SerializedName("custom_response")
                        @Expose
                        val customResponse: CustomResponse?,
                        @SerializedName("product")
                        @Expose
                        val product: Product?,
                        @SerializedName("total_bill")
                        @Expose
                        val totalBill: Int?,
                        @SerializedName("total_bill_fmt")
                        @Expose
                        val totalBillFmt: String?
                    ) {
                        class CustomResponse

                        data class Product(
                            @SerializedName("custom_response")
                            @Expose
                            val customResponse: CustomResponse?,
                            @SerializedName("title")
                            @Expose
                            val title: String?,
                            @SerializedName("total_cart")
                            @Expose
                            val totalCart: Int?,
                            @SerializedName("total_price")
                            @Expose
                            val totalPrice: Int?,
                            @SerializedName("total_price_fmt")
                            @Expose
                            val totalPriceFmt: String?,
                            @SerializedName("total_quantity")
                            @Expose
                            val totalQuantity: Int?
                        ) {
                            class CustomResponse
                        }
                    }

                    class Ticker
                }
            }
        }
    }
}
