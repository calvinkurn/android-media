package com.tokopedia.entertainment.home.data


import com.google.gson.annotations.SerializedName

data class ActionLikedRequest(
    @SerializedName("rating")
    val rating: Rating = Rating()
) {
    data class Rating(
        @SerializedName("feedback")
        val feedback: String = "",
        @SerializedName("is_liked")
        val isLiked: String = "",
        @SerializedName("product_id")
        val productId: String = "0",
        @SerializedName("rating")
        val rating: Int = 0,
        @SerializedName("user_id")
        val userId: String = "0"
    )
}