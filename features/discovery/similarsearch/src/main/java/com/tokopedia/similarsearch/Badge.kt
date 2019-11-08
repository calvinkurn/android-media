package com.tokopedia.similarsearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal class Badge(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = ""
)