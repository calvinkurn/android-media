package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Transcoding(
    @Expose @SerializedName("status") val status: String? = ""
) {

    fun isCompleted(): Boolean {
        return status == "completed"
    }

}