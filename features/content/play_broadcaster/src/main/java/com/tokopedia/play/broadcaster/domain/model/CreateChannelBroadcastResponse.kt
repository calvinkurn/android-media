package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 05/06/20.
 */
data class CreateChannelBroadcastResponse(
        @SerializedName("broadcasterCreateChannel")
        val channelId: GetChannelId
) {
    data class CreateChannelBroadcastData(

            @SerializedName("data")
            val data: CreateChannelBroadcastResponse
    )
    data class GetChannelId(
            @SerializedName("channelID")
            val channelId: String
    )
}