package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 04/12/23
 */
data class FeedGetChannelStatusEntity(
    @SerializedName("playGetChannelsStatus")
    val playGetChannelsStatus: Data = Data()
) {
    data class Data(
        @SerializedName("data")
        val data: List<ChannelStatus> = emptyList(),
    )

    data class ChannelStatus(
        @SerializedName("id")
        val channelId: String = "",
        @SerializedName("status")
        val status: String = ""
    ) {
        val isLive: Boolean
            get() = this.status.equals(STATUS_ACTIVE, true)

        companion object {
            private const val STATUS_ACTIVE = "ACTIVE"
        }


    }
}
