package com.tokopedia.chatbot.data.uploadsecure


import com.google.gson.annotations.SerializedName

data class UploadSecureResponse(
    @SerializedName("data")
    val uploadSecureData: UploadSecureData,
    @SerializedName("server")
    val server: String,
    @SerializedName("server_process_time")
    val serverProcessTime: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("success")
    val success: Int
) {
    data class UploadSecureData(
        @SerializedName("url_image")
        val urlImage: String
    )
}