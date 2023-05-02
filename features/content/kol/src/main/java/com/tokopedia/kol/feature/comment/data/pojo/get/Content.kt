package com.tokopedia.kol.feature.comment.data.pojo.get

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class Content(
    @SerializedName("imageurl")
    val imageUrl: String = "",
    @SerializedName("tags")
    val tags: List<Tag> = emptyList()
)
