package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

data class MediaItem1(
        @SerializedName("appLink")
        val appLink: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("isSelected")
        val isSelected: Boolean = false,
        @SerializedName("percentage")
        val percentage: String = "",
        @SerializedName("position")
        val position: List<Any> = listOf(),
        @SerializedName("price")
        val price: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("thumbnail")
        val thumbnail: String = "",
        @SerializedName("thumbnailLarge")
        val thumbnailLarge: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("webLink")
        val webLink: String = ""
)