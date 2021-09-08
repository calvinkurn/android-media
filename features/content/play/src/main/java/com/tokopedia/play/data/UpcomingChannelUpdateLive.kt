package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
data class UpcomingChannelUpdateLive(
    @SerializedName("channel_id".toString())
    val channelId: Long = 0,
)