package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 26/06/20.
 */

data class ChannelId(
        @SerializedName("channelID")
        val id: String = ""
)