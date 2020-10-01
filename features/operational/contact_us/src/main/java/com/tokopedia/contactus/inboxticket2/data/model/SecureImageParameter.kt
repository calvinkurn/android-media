package com.tokopedia.contactus.inboxticket2.data.model


import com.google.gson.annotations.SerializedName

data class SecureImageParameter(
        @SerializedName("data")
    val imageData: ImageData?,
        @SerializedName("message_error")
    val messageError: List<String?>?,
        @SerializedName("server_process_time")
    val serverProcessTime: String?,
        @SerializedName("status")
    val status: String?
) {
    data class ImageData(
            @SerializedName("data")
        val imageDataValues: ImageDataValues?,
            @SerializedName("is_success")
        val isSuccess: Int?
    ) {
        data class ImageDataValues(
            @SerializedName("file_name")
            val fileName: String?,
            @SerializedName("file_path")
            val filePath: String?
        )
    }
}