package com.tokopedia.mediauploader.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadHeader(
        @Expose @SerializedName("process_time") val processTime: Float = 0f,
        @Expose @SerializedName("reason") val reason: String = "",
        @Expose @SerializedName("is_success") val isSuccess: Boolean = false,
        @Expose @SerializedName("error_code") val errorCode: String = "",
        @Expose @SerializedName("messages") val messages: List<String> = emptyList()
)