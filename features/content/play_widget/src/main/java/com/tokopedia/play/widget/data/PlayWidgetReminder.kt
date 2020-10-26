package com.tokopedia.play.widget.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 21/10/20.
 */
data class PlayWidgetReminderResponse(
        @SerializedName("playToggleChannelReminder")
        val data: PlayWidgetReminder = PlayWidgetReminder()
)

data class PlayWidgetReminder(
        @SerializedName("header") val header: PlayWidgetHeaderReminder = PlayWidgetHeaderReminder()
)

data class PlayWidgetHeaderReminder(
        @SerializedName("status") val status: Int = -1,
        @SerializedName("message") val message: String = ""
)