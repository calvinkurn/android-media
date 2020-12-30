package com.tokopedia.sellerappwidget.data.model

import com.google.gson.annotations.SerializedName

data class GetOrderResponse(
        @SerializedName("orderList")
        val orderList: OrderListModel = OrderListModel(),
        @SerializedName("orderFilterSom")
        val orderFilterSom: OrderFilterSomModel? = OrderFilterSomModel()
)

data class OrderListModel(
        @SerializedName("list")
        val list: List<OrderModel> = emptyList()
)

data class OrderModel(
        @SerializedName("order_id")
        val orderId: String = "",
        @SerializedName("deadline_text")
        val deadLineText: String = "",
        @SerializedName("order_status_id")
        val statusId: Int = 0,
        @SerializedName("order_product")
        val orderProducts: List<OrderProductModel> = emptyList()
)

data class OrderProductModel(
        @SerializedName("picture")
        val picture: String = "",
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("product_name")
        val productName: String = ""
)

data class OrderFilterSomModel(
        @SerializedName("status_list")
        val statusList: List<OrderStatusModel>? = emptyList(),
)

data class OrderStatusModel(
        @SerializedName("key")
        val key: String? = "",
        @SerializedName("order_status_amount")
        val orderAmount: Int? = 0
)