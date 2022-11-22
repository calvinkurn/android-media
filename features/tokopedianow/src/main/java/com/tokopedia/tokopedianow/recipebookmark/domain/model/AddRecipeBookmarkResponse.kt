package com.tokopedia.tokopedianow.recipebookmark.domain.model

import com.google.gson.annotations.SerializedName

data class AddRecipeBookmarkResponse(
    @SerializedName("TokonowAddRecipeBookmark")
    val tokonowAddRecipeBookmark: TokonowAddRecipeBookmark
) {
    data class TokonowAddRecipeBookmark(
        @SerializedName("header")
        val header: Header
    ) {
        data class Header(
            @SerializedName("message")
            val message: String,
            @SerializedName("processTime")
            val processTime: Double,
            @SerializedName("statusCode")
            val statusCode: String,
            @SerializedName("success")
            val success: Boolean
        )
    }
}