package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-12.
 */
data class PinnedMessage(
        @SerializedName("pinned_message_id")
        val pinnedMessageId: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("redirect_url")
        val redirectUrl: String = ""
)