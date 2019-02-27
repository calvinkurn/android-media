package com.tokopedia.affiliatecommon.data.pojo.submitpost.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitPostMedium(
        @SerializedName("mediaURL")
        @Expose
        var mediaURL: String? = null,

        @SerializedName("order")
        @Expose
        var order: Int? = null) {

        @SerializedName("type")
        @Expose
        var type: String = TYPE_IMAGE

    companion object {
        private const val TYPE_IMAGE = "image"
    }
}
