package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 20/06/18.
 */

data class GetCheckWhitelistResponse(
    @SerializedName("feed_check_whitelist")
    val whitelist: Whitelist = Whitelist()
) {

    data class Whitelist(
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

        @SerializedName("has_accept_tnc")
        val hasAcceptTnc: Boolean = false,

        @SerializedName("post")
        val post: PostConfig = PostConfig(),

        @SerializedName("livestream")
        val livestream: LivestreamConfig = LivestreamConfig(),

        @SerializedName("shortvideo")
        val shortVideo: ShortVideoConfig = ShortVideoConfig(),
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

        data class ShortVideoConfig(
            @SerializedName("enable")
            val enable: Boolean = false,

            @SerializedName("has_username")
            val hasUsername: Boolean = false,
        )
    }
}
