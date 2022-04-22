package com.tokopedia.imagepicker_insta.models

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on April 21, 2022
 */
data class GetContentFormDomain(
    @SerializedName("feed_content_form")
    val feedContentForm: FeedContentForm = FeedContentForm()
) {
    data class FeedContentForm(
        @SerializedName("authors")
        val authors: List<Author> = emptyList(),
    )

    data class Author(
        @SerializedName("type")
        val type: String = "",

        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("thumbnail")
        val thumbnail: String = "",

        @SerializedName("badge")
        val badge: String = "",
    )
}