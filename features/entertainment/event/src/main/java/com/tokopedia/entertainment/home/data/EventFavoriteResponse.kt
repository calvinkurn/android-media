package com.tokopedia.entertainment.home.data


import com.google.gson.annotations.SerializedName

data class EventFavoriteResponse(
    @SerializedName("data")
    val `data`: List<Data> = listOf()
) {
    data class Data(
        @SerializedName("category_id")
        val categoryId: String = "0",
        @SerializedName("city")
        val city: String = "",
        @SerializedName("display_name")
        val displayName: String = "",
        @SerializedName("end_date")
        val endDate: Int = 0,
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("is_liked")
        var isLiked: Boolean = false,
        @SerializedName("product_id")
        val productId: String = "0",
        @SerializedName("sales_price")
        val salesPrice: Int = 0,
        @SerializedName("start_date")
        val startDate: Int = 0,
        @SerializedName("url")
        val url: String = ""
    )
}