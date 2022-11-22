package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 20/06/18.
 */

data class GetCheckWhitelistResponse (
    @SerializedName("feed_check_whitelist")
    val whitelist: Whitelist = Whitelist()
) {

    data class Whitelist (
        @SerializedName("iswhitelist")
        val isWhitelist: Boolean = false,

        @SerializedName("url")
        val url: String = "",

        @SerializedName("error")
        val error: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("title_identifier")
        val titleIdentifier: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName("post_success")
        val postSuccessMessage: String = "",

        @SerializedName("image_url")
        val imageUrl: String = "",

        @SerializedName("authors")
        val authors: List<Author> = ArrayList()
    )
    data class Author(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("thumbnail")
        val thumbnail: String = "",

        @SerializedName("link")
        val link: String = "",

        @SerializedName("badge")
        val badge: String = "",

        @SerializedName("type")
        val type: String = "",

        @SerializedName("post")
        val post: PostConfig = PostConfig(),

        @SerializedName("livestream")
        val livestream: LivestreamConfig = LivestreamConfig(),
    ) {

        data class PostConfig(
            @SerializedName("enable")
            val enable: Boolean = false,

            @SerializedName("has_username")
            val hasUsername: Boolean = false,
        )

        data class LivestreamConfig(
            @SerializedName("enable")
            val enable: Boolean = false,

            @SerializedName("has_username")
            val hasUsername: Boolean = false,
        )

        companion object {
            const val TYPE_AFFILIATE = "affiliate"
            const val TYPE_SHOP = "content-shop"
            const val TYPE_USER = "content-user"
            const val KEY_POST_TOKO = "Post Toko"
        }
    }
}
