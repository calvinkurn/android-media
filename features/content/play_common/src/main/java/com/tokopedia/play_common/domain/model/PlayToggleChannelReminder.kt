package com.tokopedia.play_common.domain.model

import com.google.gson.annotations.SerializedName

data class PlayToggleChannelReminder(
        @SerializedName("header")
    val header: PlayToggleChannelReminderHeader = PlayToggleChannelReminderHeader()
){
   data class PlayToggleChannelReminderHeader(
        @SerializedName("status")
        val status: PlayToggleChannelReminderStatus = PlayToggleChannelReminderStatus()
    )

    data class PlayToggleChannelReminderStatus(
            val status: String = ""
    )
}