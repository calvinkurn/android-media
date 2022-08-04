package com.tokopedia.kolcommon.data

import com.google.gson.annotations.SerializedName

/**
 * Created by meyta.taliti on 03/08/22.
 */
data class SubmitActionContentResponse(
    @SerializedName("feed_content_submit")
    val content: FeedContentSubmit = FeedContentSubmit()
) {
    data class FeedContentSubmit(
        @SerializedName("success")
        val success: Int = 0,

        @SerializedName("redirectURI")
        val redirectURI: String = "",

        @SerializedName("error")
        val error: String = "",

        @SerializedName("meta")
        val meta: Meta = Meta()
    )

    data class Meta(
        @SerializedName("content")
        val content: Content = Content()
    )

    data class Content(
        @SerializedName("activityID")
        val activityID: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName("url")
        val url: String = ""
    )
}