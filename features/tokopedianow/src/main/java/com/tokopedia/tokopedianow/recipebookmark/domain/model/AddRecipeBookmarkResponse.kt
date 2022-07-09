package com.tokopedia.tokopedianow.recipebookmark.domain.model

import com.google.gson.annotations.SerializedName

data class AddRecipeBookmarkResponse(
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("TokonowAddRecipeBookmark")
        val tokonowAddRecipeBookmark: TokonowAddRecipeBookmark
    ) {
        data class TokonowAddRecipeBookmark(
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