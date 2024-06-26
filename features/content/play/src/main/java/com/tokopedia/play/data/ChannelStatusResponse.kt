package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 01/02/21.
 */
data class ChannelStatusResponse(
        @SerializedName("playGetChannelsStatus")
        val playGetChannelsStatus: Data = Data()
) {

    data class Data(
            @SerializedName("data")
            val data: List<ChannelStatus> = listOf(),

            @SerializedName("waiting_duration")
            val waitingDuration: Int = 0,
    )

    data class ChannelStatus(
            @SerializedName("id")
            val channelId: String = "",
            @SerializedName("status")
            val status: String = "",
    )
}
