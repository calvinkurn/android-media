package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

data class VideoPostReimderModel(
    @SerializedName("playToggleChannelReminder")
    val playToggleChannelReminder: PlayToggleChannelReminderData,
)

data class PlayToggleChannelReminderData(
    @SerializedName("header")
    val header: ChannelReminderDataHeader,
)

data class ChannelReminderDataHeader(
    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: Int,
)
