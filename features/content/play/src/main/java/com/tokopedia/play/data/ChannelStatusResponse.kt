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

            @SerializedName("archivedConfig")
            val archivedConfig: ArchivedData = ArchivedData(),
    )

    data class ChannelStatus(
            @SerializedName("id")
            val channelId: String = "",
            @SerializedName("status")
            val status: String = "",
    )

    data class ArchivedData(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName("button_text")
        val buttonText: String = "",

        @SerializedName("button_app_link")
        val appLink: String = "",
    )
}
