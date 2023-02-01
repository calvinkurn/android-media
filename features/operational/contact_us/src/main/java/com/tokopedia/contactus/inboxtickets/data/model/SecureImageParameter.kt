package com.tokopedia.contactus.inboxtickets.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.FAILED_KEY_SECURE_IMAGE_PARAMETER

data class SecureImageParameter(
    @SerializedName("data")
    val imageData: ImageData? = null,
    @SerializedName("message_error")
    val messageError: List<String?>? = null,
    @SerializedName("server_process_time")
    val serverProcessTime: String? = null,
    @SerializedName("status")
    val status: String? = null
) {
    data class ImageData(
        @SerializedName("data")
        val imageDataValues: ImageDataValues? = null,
        @SerializedName("is_success")
        val isSuccess: Int? = null
    ) {
        data class ImageDataValues(
            @SerializedName("file_name")
            val fileName: String? = null,
            @SerializedName("file_path")
            val filePath: String? = null
        )

        fun isSuccess() = isSuccess ?: FAILED_KEY_SECURE_IMAGE_PARAMETER
    }

    fun getImage() = imageData ?: ImageData()
}
