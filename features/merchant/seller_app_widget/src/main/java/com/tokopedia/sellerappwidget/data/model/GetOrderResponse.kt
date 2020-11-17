package com.tokopedia.sellerappwidget.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetOrderResponse(
        @Expose
        @SerializedName("orderList")
        val orderList: OrderListModel = OrderListModel()
)

data class OrderListModel(
        @Expose
        @SerializedName("list")
        val list: List<OrderModel> = emptyList()
)

data class OrderModel(
        @Expose
        @SerializedName("order_id")
        val orderId: String = "",
        @Expose
        @SerializedName("deadline_text")
        val deadLineText: String = "",
        @Expose
        @SerializedName("order_status_id")
        val statusId: Int = 0,
        @Expose
        @SerializedName("order_product")
        val orderProducts: List<OrderProductModel> = emptyList()
)

data class OrderProductModel(
        @Expose
        @SerializedName("picture")
        val picture: String = "",
        @Expose
        @SerializedName("product_id")
        val productId: String = "",
        @Expose
        @SerializedName("product_name")
        val productName: String = ""
)