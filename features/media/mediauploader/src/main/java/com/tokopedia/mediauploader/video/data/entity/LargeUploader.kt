package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LargeUploader(
    @Expose @SerializedName("success") val success: Boolean? = false,
    @Expose @SerializedName("part_success") val partSuccess: Boolean? = false,
    @Expose @SerializedName("upload_id") val uploadId: String? = "",
    @Expose @SerializedName("video_url") val videoUrl: String? = ""
) {

    fun isSuccess() = success == true

    fun isPartSuccess() = partSuccess == true

    fun uploadId() = uploadId?: ""

    fun videoUrl() = videoUrl?: ""

}