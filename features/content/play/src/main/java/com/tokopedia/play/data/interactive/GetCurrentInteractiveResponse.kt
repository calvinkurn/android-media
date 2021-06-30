package com.tokopedia.play.data.interactive

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 28/06/21
 */
data class GetCurrentInteractiveResponse(
        @SerializedName("playInteractiveGetCurrentInteractive")
        val data: Data = Data()
) {
    data class Data(
            @SerializedName("interactive")
            val interactive: ChannelInteractive = ChannelInteractive()
    )
}

data class ChannelInteractive(
        @SerializedName("channel_id")
        val channelID: Long = 0,

        @SerializedName("interactive_id")
        val interactiveID: Long = 0,

        @SerializedName("interactive_type")
        val interactiveType: Int = -1,

        @SerializedName("title")
        val title: String = "",

        @SerializedName("status")
        val status: Int = 0,

        @SerializedName("countdown_start")
        val countdownStart: Int = 0,

        @SerializedName("countdown_end")
        val countdownEnd: Int = 0,
)