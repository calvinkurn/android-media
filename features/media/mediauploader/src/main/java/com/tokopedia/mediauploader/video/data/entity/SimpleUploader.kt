package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SimpleUploader(
    @Expose @SerializedName("success") val success: Boolean? = false,
    @Expose @SerializedName("upload_id") val uploadId: String? = "",
    @Expose @SerializedName("video_url") val videoUrl: String? = "",
    @Expose @SerializedName("status") val status: Int? = 0,
    @Expose @SerializedName("error_message") val errorMessage: String? = "",
)