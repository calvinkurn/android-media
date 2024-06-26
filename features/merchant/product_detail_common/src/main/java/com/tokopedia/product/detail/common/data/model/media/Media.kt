package com.tokopedia.product.detail.common.data.model.media

import com.google.gson.annotations.SerializedName

data class Media(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("isAutoplay")
        val isAutoplay: Boolean = false,
        @SerializedName("type")
        val type: String = "",
        @SerializedName("URL300")
        val uRL300: String = "",
        @SerializedName("URLOriginal")
        val uRLOriginal: String = "",
        @SerializedName("URLThumbnail")
        val uRLThumbnail: String = "",
        @SerializedName("videoURLAndroid")
        val videoURLAndroid: String = "",
        @SerializedName("variantOptionID")
        val variantOptionId: String = "",
        @SerializedName("URLMaxRes")
        val urlHD: String = ""
) {
    var id: String = ""
    var prefetch: Boolean = false
}
