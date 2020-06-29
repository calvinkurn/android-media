package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 05/06/20.
 */
data class CreateChannelBroadcastResponse(
        @SerializedName("broadcasterCreateChannel")
        val getChannelId: ChannelId
)