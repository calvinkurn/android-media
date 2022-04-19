package com.tokopedia.play_common.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play_common.domain.model.ChannelId


/**
 * Created by mzennis on 26/06/20.
 */
data class UpdateChannelResponse(
        @SerializedName("broadcasterUpdateChannel")
        val updateChannel: ChannelId = ChannelId()
)