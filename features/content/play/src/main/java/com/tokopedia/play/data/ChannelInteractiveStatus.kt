package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 30/06/21
 */
data class ChannelInteractiveStatus(
        @SerializedName("channel_id")
        val channelId: Long = 0L,

        @SerializedName("exist")
        val isExist: Boolean = false,
)