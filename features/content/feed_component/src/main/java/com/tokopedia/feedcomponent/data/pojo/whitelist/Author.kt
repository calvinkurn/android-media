package com.tokopedia.feedcomponent.data.pojo.whitelist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 27/03/19.
 */
data class Author(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = "",

        @SerializedName("link")
        @Expose
        val link: String = "",

        @SerializedName("badge")
        @Expose
        val badge: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("post")
        @Expose
        val post: PostConfig = PostConfig(),

        @SerializedName("livestream")
        @Expose
        val livestream: LivestreamConfig = LivestreamConfig(),
) {

        data class PostConfig(
                @SerializedName("enable")
                @Expose
                val enable: Boolean = false,

                @SerializedName("has_username")
                @Expose
                val hasUsername: Boolean = false,
        )

        data class LivestreamConfig(
                @SerializedName("enable")
                @Expose
                val enable: Boolean = false,

                @SerializedName("has_username")
                @Expose
                val hasUsername: Boolean = false,
        )

    companion object {
        var TYPE_AFFILIATE = "affiliate"
        var TYPE_SHOP = "content-shop"
        var TYPE_USER = "content-user"
        var KEY_POST_TOKO = "Post Toko"
    }
}
