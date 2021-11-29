package com.tokopedia.play.broadcaster.domain.model.socket

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 13/10/21
 */
data class PinnedMessageSocketResponse(
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