package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.SerializedName

data class Transcoding(
    @SerializedName("status") val status: String? = "",
    @SerializedName("request_id") val requestId: String? = "",
) {

    fun isCompleted(): Boolean {
        return status == "completed"
    }

    fun requestId() = requestId ?: ""
}
