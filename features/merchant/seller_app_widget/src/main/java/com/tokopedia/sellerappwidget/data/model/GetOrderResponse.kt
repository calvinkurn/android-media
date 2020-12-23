package com.tokopedia.sellerappwidget.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetOrderResponse(
        @Expose
        @SerializedName("orderList")
        val orderList: OrderListModel = OrderListModel(),
        @Expose
        @SerializedName("orderFilterSom")
        val orderFilterSom: OrderFilterSomModel? = OrderFilterSomModel()
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

data class OrderFilterSomModel(
        @Expose
        @SerializedName("status_list")
        val statusList: List<OrderStatusModel>? = emptyList(),
)

data class OrderStatusModel(
        @Expose
        @SerializedName("key")
        val key: String? = "",
        @Expose
        @SerializedName("order_status_amount")
        val orderAmount: Int? = 0
)