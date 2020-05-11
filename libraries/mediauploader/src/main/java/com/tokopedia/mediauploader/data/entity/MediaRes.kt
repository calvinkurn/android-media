package com.tokopedia.mediauploader.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MediaRes(
        @Expose @SerializedName("w") val width: Int = 0,
        @Expose @SerializedName("h") val height: Int = 0
)