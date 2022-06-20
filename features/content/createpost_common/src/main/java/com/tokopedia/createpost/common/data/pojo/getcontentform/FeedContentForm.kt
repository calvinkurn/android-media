package com.tokopedia.createpost.common.data.pojo.getcontentform

import com.google.gson.annotations.SerializedName

data class FeedContentForm(
    @SerializedName("authors")
    val authors: List<Author> = emptyList(),

    @SerializedName("error")
    val error: String = "",

    @SerializedName("maxTag")
    val maxTag: Int = 1,

    @SerializedName("media")
    val media: Media = Media(),

    @SerializedName("relatedItems")
    val relatedItems: List<RelatedItem> = emptyList(),

    @SerializedName("token")
    val token: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("defaultPlaceholder")
    val defaultPlaceholder: String = "",

    @SerializedName("caption")
    val caption: String = "",

    @SerializedName("defaultCaptions")
    val defaultCaptions: List<String> = emptyList(),

    @SerializedName("productTagSources")
    val productTagSources: List<String> = emptyList(),

    @SerializedName("has_username")
    val hasUsername: Boolean = false,

    @SerializedName("has_accept_tnc")
    val hasAcceptTnc: Boolean = false,
)