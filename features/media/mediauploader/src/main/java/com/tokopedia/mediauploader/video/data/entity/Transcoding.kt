package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Transcoding(
    @SerializedName("status") val status: String? = "",
    @SerializedName("request_id") val requestId: String? = "",
) {

    fun isCompleted(): Boolean {
        return status?.lowercase(Locale.US) == "completed"
    }

    fun requestId() = requestId ?: ""

    fun isFailed(): Boolean {
        return status?.lowercase(Locale.US) == "failed"
    }
}
