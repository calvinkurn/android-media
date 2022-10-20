package com.tokopedia.tokopedianow.recipebookmark.domain.model

import com.google.gson.annotations.SerializedName

data class RemoveRecipeBookmarkResponse(
    @SerializedName("TokonowRemoveRecipeBookmark")
    val tokonowRemoveRecipeBookmark: TokonowRemoveRecipeBookmark
) {
    data class TokonowRemoveRecipeBookmark(
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