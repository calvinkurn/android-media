package com.tokopedia.play.widget.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 21/10/20.
 */
data class PlayWidgetReminder(
        @SerializedName("playToggleChannelReminder")
        val playToggleChannelReminder: ToggleChannelReminder = ToggleChannelReminder()
) {
        data class ToggleChannelReminder(
                @SerializedName("header") val header: Header = Header()
        )

        data class Header(
                @SerializedName("status") val status: Int = -1,
                @SerializedName("message") val message: String = ""
        )
}