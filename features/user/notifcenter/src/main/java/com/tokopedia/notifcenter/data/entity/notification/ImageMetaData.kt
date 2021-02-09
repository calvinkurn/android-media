package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class ImageMetaData(
        @SerializedName("height")
        val height: Int = 0,
        @SerializedName("ratio")
        val ratio: Ratio = Ratio(),
        @SerializedName("url")
        val url: String = "",
        @SerializedName("width")
        val width: Int = 0
)