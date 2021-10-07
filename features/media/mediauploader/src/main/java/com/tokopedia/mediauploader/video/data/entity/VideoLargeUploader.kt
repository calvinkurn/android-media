package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoLargeUploader(
    @Expose @SerializedName("success") val success: Boolean = false,
    @Expose @SerializedName("upload_id") val uploadId: String? = ""
)