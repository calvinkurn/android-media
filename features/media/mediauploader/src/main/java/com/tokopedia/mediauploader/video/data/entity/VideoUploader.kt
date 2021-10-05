package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoUploader(
    @Expose @SerializedName("success") val success: Boolean = false,
    @Expose @SerializedName("upload_id") val uploadId: String = "",
    @Expose @SerializedName("video_url") val videoUrl: String = ""
)