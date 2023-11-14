package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR

data class SimpleUploader(
    @Expose @SerializedName("success") val success: Boolean? = false,
    @Expose @SerializedName("upload_id") val uploadId: String? = "",
    @Expose @SerializedName("video_url") val videoUrl: String? = "",
    @Expose @SerializedName("status") val status: Int? = 0,
    @Expose @SerializedName("error_message") val errorMessage: String? = "",
    @Expose @SerializedName("request_id") val requestId: String? = "",
) {

    fun errorMessage(): String {
        return if (errorMessage.isNullOrEmpty().not()) {
            errorMessage ?: ""
        } else {
            UNKNOWN_ERROR
        }
    }
}
