package com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform

import com.google.gson.annotations.SerializedName

data class MediaItem(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("media_url")
        val mediaUrl: String = "",
        @SerializedName("removable")
        val removable: Boolean = false,
        @SerializedName("type")
        val type: String = ""
)