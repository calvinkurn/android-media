package com.tokopedia.entertainment.home.data


import com.google.gson.annotations.SerializedName

data class ActionLikedResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("server_process_time")
    val serverProcessTime: String = "",
    @SerializedName("status")
    val status: String = ""
) {
    data class Data(
        @SerializedName("category_id")
        val categoryId: String = "0",
        @SerializedName("created_at")
        val createdAt: String = "",
        @SerializedName("feedback")
        val feedback: String = "",
        @SerializedName("id")
        val id: String = "0",
        @SerializedName("is_liked")
        val isLiked: Boolean = false,
        @SerializedName("product_id")
        val productId: String = "0",
        @SerializedName("rating")
        val rating: Int = 0,
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("updated_at")
        val updatedAt: String = "",
        @SerializedName("user_id")
        val userId: String = "0"
    )
}