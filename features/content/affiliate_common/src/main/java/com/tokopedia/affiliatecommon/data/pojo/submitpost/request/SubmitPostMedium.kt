package com.tokopedia.affiliatecommon.data.pojo.submitpost.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitPostMedium(
        @SerializedName("mediaURL")
        @Expose
        var mediaURL: String = "",

        @SerializedName("order")
        @Expose
        val order: Int = 0,

        @SerializedName("tags")
        @Expose
        var tags: List<MediaTag> = arrayListOf(),

        @SerializedName("type")
        @Expose
        val type: String = TYPE_IMAGE

) {
    companion object {
        const val TYPE_IMAGE = "image"
        const val TYPE_VIDEO = "video"

    }
}
