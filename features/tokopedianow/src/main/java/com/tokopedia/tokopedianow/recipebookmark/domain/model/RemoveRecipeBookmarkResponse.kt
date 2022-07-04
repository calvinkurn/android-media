package com.tokopedia.tokopedianow.recipebookmark.domain.model

import com.google.gson.annotations.SerializedName

data class RemoveRecipeBookmarkResponse(
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("TokonowRemoveRecipeBookmark")
        val tokonowRemoveRecipeBookmark: TokonowRemoveRecipeBookmark
    ) {
        data class TokonowRemoveRecipeBookmark(
            @SerializedName("header")
            val header: Header,
            @SerializedName("success")
            val success: Boolean
        ) {
            data class Header(
                @SerializedName("error_code")
                val errorCode: String,
                @SerializedName("message")
                val message: String,
                @SerializedName("process_time")
                val processTime: Double,
                @SerializedName("reason")
                val reason: String
            )
        }
    }
}