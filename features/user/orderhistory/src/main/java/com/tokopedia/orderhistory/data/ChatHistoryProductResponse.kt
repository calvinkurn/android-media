package com.tokopedia.orderhistory.data


import com.google.gson.annotations.SerializedName

data class ChatHistoryProductResponse(
        @SerializedName("chatHistoryProducts")
        val chatHistoryProducts: ChatHistoryProducts = ChatHistoryProducts()
) {
    val products: List<Product> get() = chatHistoryProducts.products
    val hasNext: Boolean get() = chatHistoryProducts.hasNext
}