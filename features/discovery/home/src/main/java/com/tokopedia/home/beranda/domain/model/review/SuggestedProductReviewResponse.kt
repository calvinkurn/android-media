package com.tokopedia.home.beranda.domain.model.review


import com.google.gson.annotations.SerializedName

data class SuggestedProductReviewResponse(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("dismissURL")
        val dismissURL: String = "",
        @SerializedName("dismissable")
        val dismissable: Boolean = false,
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("linkURL")
        val linkURL: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("orderID")
        val orderId: String = "",
        @SerializedName("productID")
        val productId: String = ""
)