package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play_common.domain.model.ChannelId


/**
 * Created by mzennis on 05/06/20.
 */
data class CreateChannelBroadcastResponse(
        @SerializedName("broadcasterCreateChannel")
        val getChannelId: ChannelId
)