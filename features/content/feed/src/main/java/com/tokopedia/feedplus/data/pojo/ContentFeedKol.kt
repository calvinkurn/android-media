package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class ContentFeedKol(
    @SerializedName("imageurl")
    @Expose
    val imageUrl: String = "",

    @SerializedName("youtube")
    @Expose
    val youtube: String = "",

    @SerializedName("video")
    @Expose
    val video: String = "",

    @SerializedName("type")
    @Expose
    val type: String = "",

    @SerializedName("tags")
    @Expose
    val tags: List<TagsFeedKol> = emptyList()
)