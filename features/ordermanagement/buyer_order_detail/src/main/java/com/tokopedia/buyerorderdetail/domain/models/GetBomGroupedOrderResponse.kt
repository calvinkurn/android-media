package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.SerializedName

data class GetBomGroupedOrderResponse(
    @SerializedName("get_bom_grouped_order")
    val getBomGroupedOrder: GetBomGroupedOrder = GetBomGroupedOrder()
) {
    data class GetBomGroupedOrder(
        @SerializedName("orders")
        val orders: List<Order> = listOf(),
        @SerializedName("ticker")
        val ticker: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo? = GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo(),
        @SerializedName("title")
        val title: String = "",
        @SerializedName("tx_id")
        val txId: String = "0"
    ) {
        data class Order(
            @SerializedName("buttons")
            val button: Button = Button(),
            @SerializedName("details")
            val details: Details = Details(),
            @SerializedName("invoice")
            val invoice: String = "",
            @SerializedName("order_id")
            val orderId: String = "0",
            @SerializedName("shop")
            val shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop = GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop()
        ) {
            data class Button(
                @SerializedName("display_name")
                val displayName: String = "",
                @SerializedName("key")
                val key: String = "",
                @SerializedName("type")
                val type: String = "",
                @SerializedName("url")
                val url: String = "",
                @SerializedName("variant")
                val variant: String = ""
            )

            data class Details(
                @SerializedName("addon_icon")
                val addonIcon: String = "",
                @SerializedName("addon_label")
                val addonLabel: String = "",
                @SerializedName("bundle")
                val bundle: List<Bundle>? = listOf(),
                @SerializedName("bundle_icon")
                val bundleIcon: String = "",
                @SerializedName("non_bundle")
                val nonBundle: List<NonBundle>? = listOf(),
                @SerializedName("order_addons")
                val orderAddons: List<Addon>? = listOf()
            ) {
                data class Bundle(
                    @SerializedName("bundle_id")
                    val bundleId: String = "0",
                    @SerializedName("bundle_name")
                    val bundleName: String = "",
                    @SerializedName("bundle_variant_id")
                    val bundleVariantId: String = "0",
                    @SerializedName("order_detail")
                    val orderDetail: List<OrderDetail> = listOf()
                ) {
                    data class OrderDetail(
                        @SerializedName("order_detail_id")
                        val orderDetailId: String = "0",
                        @SerializedName("price_text")
                        val priceText: String = "",
                        @SerializedName("product_id")
                        val productId: String = "0",
                        @SerializedName("product_name")
                        val productName: String = "",
                        @SerializedName("quantity")
                        val quantity: Int = 0,
                        @SerializedName("thumbnail")
                        val thumbnail: String = ""
                    )
                }

                data class NonBundle(
                    @SerializedName("addon")
                    val addon: List<Addon>? = listOf(),
                    @SerializedName("order_detail_id")
                    val orderDetailId: String = "0",
                    @SerializedName("price_text")
                    val priceText: String = "",
                    @SerializedName("product_id")
                    val productId: String = "0",
                    @SerializedName("product_name")
                    val productName: String = "",
                    @SerializedName("quantity")
                    val quantity: Int = 0,
                    @SerializedName("thumbnail")
                    val thumbnail: String = ""
                )

                data class Addon(
                    @SerializedName("id")
                    val id: String = "0",
                    @SerializedName("image_url")
                    val imageUrl: String = "",
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("order_id")
                    val orderId: String = "0",
                    @SerializedName("price")
                    val price: String = "",
                    @SerializedName("quantity")
                    val quantity: Int = 0,
                    @SerializedName("type")
                    val type: String = ""
                )
            }
        }
    }
}
