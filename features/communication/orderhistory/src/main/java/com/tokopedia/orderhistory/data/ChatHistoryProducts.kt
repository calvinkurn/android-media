package com.tokopedia.orderhistory.data


import com.google.gson.annotations.SerializedName

data class ChatHistoryProducts(
        @SerializedName("hasNext")
        val hasNext: Boolean = false,
        @SerializedName("minOrderTime")
        val minOrderTime: String = "",
        @SerializedName("products")
        val products: List<Product> = listOf()
)