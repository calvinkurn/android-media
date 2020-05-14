package com.tokopedia.sellerorder.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-08-29.
 */
data class SomListOrder (
        @SerializedName("data")
        @Expose
        val data: Data = Data()
) {
    data class Data (
            @SerializedName("orderList")
            @Expose
            val orderList: OrderList = OrderList()
    ) {
        data class OrderList (
                @SerializedName("cursor_order_id")
                @Expose
                val cursorOrderId: Int = -1,

                @SerializedName("list")
                @Expose
                val orders: List<Order> = listOf()
        ) {
            data class Order (
                    @SerializedName("order_id")
                    @Expose
                    val orderId: String = "",

                    @SerializedName("order_status_id")
                    @Expose
                    val orderStatusId: Int = -1,

                    @SerializedName("status")
                    @Expose
                    val status: String = "",

                    @SerializedName("status_color")
                    @Expose
                    val statusColor: String = "",

                    @SerializedName("order_resi")
                    @Expose
                    val orderResi: String = "",

                    @SerializedName("order_date")
                    @Expose
                    val orderDate: String = "",

                    @SerializedName("order_label")
                    @Expose
                    val listOrderLabel: List<OrderLabel> = listOf(),

                    @SerializedName("buyer_name")
                    @Expose
                    val buyerName: String = "",

                    @SerializedName("deadline_text")
                    @Expose
                    val deadlineText: String = "",

                    @SerializedName("deadline_color")
                    @Expose
                    val deadlineColor: String = "",

                    @SerializedName("order_product")
                    @Expose
                    val listOrderProduct: List<OrderProduct> = listOf(),

                    @SerializedName("cancel_request")
                    @Expose
                    val cancelRequest: Int = -1,

                    @SerializedName("cancel_request_note")
                    @Expose
                    val cancelRequestNote: String = "",

                    @SerializedName("cancel_request_time")
                    @Expose
                    val cancelRequestTime: String = "",

                    @SerializedName("cancel_request_origin_note")
                    @Expose
                    val cancelRequestOriginNote: String = ""
            ) {
                data class OrderLabel (
                        @SerializedName("flag_name")
                        @Expose
                        val flagName: String = "",

                        @SerializedName("flag_color")
                        @Expose
                        val flagColor: String = "",

                        @SerializedName("flag_background")
                        @Expose
                        val flagBackground: String = ""
                )

                data class OrderProduct (
                        @SerializedName("picture")
                        @Expose
                        val pictureUrl: String = "",

                        @SerializedName("product_name")
                        @Expose
                        val productName: String = ""
                )
            }
        }
    }
}