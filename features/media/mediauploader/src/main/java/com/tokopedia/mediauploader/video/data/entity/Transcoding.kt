package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.mediauploader.video.data.state.TranscodingState

data class Transcoding(
    @Expose @SerializedName("status") val status: String? = ""
) {

    fun status(): TranscodingState {
        if (status == null) return TranscodingState.UNKNOWN
        return TranscodingState.valueOf(status)
    }

}