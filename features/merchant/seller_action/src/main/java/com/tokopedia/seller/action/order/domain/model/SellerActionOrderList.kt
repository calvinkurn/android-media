package com.tokopedia.seller.action.order.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem

data class SellerActionOrder (
        @SerializedName("orderList")
        @Expose
        val orderList: OrderList = OrderList()
)

data class OrderList (
        @SerializedName("list")
        @Expose
        val orders: List<Order> = listOf()
)

data class Order (
        @SerializedName("order_id")
        @Expose
        val orderId: String = "",

        @SerializedName("status")
        @Expose
        val status: String = "",

        @SerializedName("order_status_id")
        @Expose
        val orderStatusId: Int = 0,

        @SerializedName("deadline_text")
        @Expose
        val deadlineText: String = "",

        @SerializedName("order_date")
        @Expose
        val orderDate: String = "",

        @SerializedName("buyer_name")
        @Expose
        val buyerName: String = "",

        @SerializedName("order_product")
        @Expose
        val listOrderProduct: List<OrderProduct> = listOf()
): SellerSuccessItem

data class OrderProduct (
        @SerializedName("picture")
        @Expose
        val pictureUrl: String = "",
        @SerializedName("product_name")
        @Expose
        val productName: String = ""
)