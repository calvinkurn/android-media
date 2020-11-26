package com.tokopedia.play_common.domain.model

import com.google.gson.annotations.SerializedName

data class PlayToggleChannelEntity(
        @SerializedName("playToggleChannelReminder")
        val playToggleChannelReminder: PlayToggleChannelReminder?
)

data class PlayToggleChannelReminder(
        @SerializedName("header")
    val header: PlayToggleChannelReminderHeader = PlayToggleChannelReminderHeader()
){
    companion object{
        const val SUCCESS_STATUS = 200
    }
   data class PlayToggleChannelReminderHeader(
        @SerializedName("status")
        val status: Int = -1,
        @SerializedName("message")
        val message: String = ""
    )
}