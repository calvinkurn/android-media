package com.tokopedia.mediauploader.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadHeader(
        @Expose @SerializedName("process_time") val processTime: Int = 0,
        @Expose @SerializedName("reason") val reason: String = "",
        @Expose @SerializedName("error_code") val errorCode: String = ""
)