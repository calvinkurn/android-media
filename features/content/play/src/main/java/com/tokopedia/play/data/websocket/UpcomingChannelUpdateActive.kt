package com.tokopedia.play.data.websocket

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
data class UpcomingChannelUpdateActive(
    @SerializedName("channel_id".toString())
    val channelId: Long = 0,
)