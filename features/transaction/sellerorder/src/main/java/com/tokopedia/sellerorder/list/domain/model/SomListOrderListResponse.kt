package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.common.presenter.model.PopUp

data class SomListOrderListResponse(
        @SerializedName("data")
        @Expose
        val `data`: Data = Data()
) {
    data class Data(
            @SerializedName("orderList")
            @Expose
            val orderList: OrderList = OrderList()
    ) {
        data class OrderList(
                @SerializedName("cursor_order_id")
                @Expose
                val cursorOrderId: String = "0",
                @SerializedName("list")
                @Expose
                val list: List<Order> = listOf()
        ) {
            data class Order(
                    @SerializedName("cancel_request")
                    @Expose
                    val cancelRequest: Int = 0,
                    @SerializedName("cancel_request_note")
                    @Expose
                    val cancelRequestNote: String = "",
                    @SerializedName("cancel_request_origin_note")
                    @Expose
                    val cancelRequestOriginNote: String = "",
                    @SerializedName("cancel_request_time")
                    @Expose
                    val cancelRequestTime: String = "",
                    @SerializedName("cancel_request_status")
                    @Expose
                    val cancelRequestStatus: Int = 0,
                    @SerializedName("deadline_color")
                    @Expose
                    val deadlineColor: String = "",
                    @SerializedName("deadline_text")
                    @Expose
                    val deadlineText: String = "",
                    @SerializedName("order_id")
                    @Expose
                    val orderId: String = "",
                    @SerializedName("order_product")
                    @Expose
                    val orderProduct: List<OrderProduct> = listOf(),
                    @SerializedName("order_resi")
                    @Expose
                    val orderResi: String = "",
                    @SerializedName("order_status_id")
                    @Expose
                    val orderStatusId: String = "0",
                    @SerializedName("status")
                    @Expose
                    val status: String = "",
                    @SerializedName("status_color")
                    @Expose
                    val statusColor: String = "",
                    @SerializedName("status_indicator_color")
                    @Expose
                    val statusIndicatorColor: String = "",
                    @SerializedName("destination_province")
                    @Expose
                    val destinationProvince: String = "",
                    @SerializedName("courier_name")
                    @Expose
                    val courierName: String = "",
                    @SerializedName("courier_product_name")
                    @Expose
                    val courierProductName: String = "",
                    @SerializedName("preorder_type")
                    @Expose
                    val preOrderType: Int = 0,
                    @SerializedName("buyer_name")
                    @Expose
                    val buyerName: String = "",
                    @SerializedName("ticker_info")
                    @Expose
                    val tickerInfo: TickerInfo = TickerInfo(),
                    @SerializedName("button")
                    @Expose
                    val buttons: List<Button> = emptyList()
            ) {
                data class OrderProduct(
                        @SerializedName("product_id")
                        @Expose
                        val productId: String = "",
                        @SerializedName("product_name")
                        @Expose
                        val productName: String = "",
                        @SerializedName("picture")
                        @Expose
                        val picture: String = ""
                )

                data class Button(
                        @SerializedName("key")
                        @Expose
                        val key: String = "",
                        @SerializedName("display_name")
                        @Expose
                        val displayName: String = "",
                        @SerializedName("type")
                        @Expose
                        val type: String = "",
                        @SerializedName("url")
                        @Expose
                        val url: String = "",
                        @SerializedName("popup")
                        @Expose
                        val popUp: PopUp = PopUp()
                )
            }
        }
    }
}