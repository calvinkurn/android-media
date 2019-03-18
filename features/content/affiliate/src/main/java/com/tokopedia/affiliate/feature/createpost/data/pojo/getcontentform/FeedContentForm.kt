package com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform

import com.google.gson.annotations.SerializedName

data class FeedContentForm(
        @SerializedName("authors")
        val authors: List<Author> = listOf(),
        @SerializedName("error")
        val error: String = "",
        @SerializedName("maxTag")
        val maxTag: Int = 1,
        @SerializedName("media")
        val media: Media = Media(),
        @SerializedName("relatedItems")
        val relatedItems: List<RelatedItem> = listOf(),
        @SerializedName("token")
        val token: String = "",
        @SerializedName("type")
        val type: String = ""
)