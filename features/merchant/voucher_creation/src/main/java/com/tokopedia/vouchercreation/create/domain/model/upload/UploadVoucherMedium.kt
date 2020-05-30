package com.tokopedia.vouchercreation.create.domain.model.upload

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadVoucherMedium(
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
        val type: String = TYPE_IMAGE) {

    companion object {
        const val TYPE_IMAGE = "image"
    }
}

data class MediaTag (
        @SerializedName("tagType")
        @Expose
        val type: String = "",

        @SerializedName("tagContent")
        @Expose
        val content: String = ""
)