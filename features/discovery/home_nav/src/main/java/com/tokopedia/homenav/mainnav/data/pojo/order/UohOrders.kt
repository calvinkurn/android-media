package com.tokopedia.homenav.mainnav.data.pojo.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UohOrders(
        @SerializedName("orders")
        @Expose
        val orders: List<Order>? = listOf()
)