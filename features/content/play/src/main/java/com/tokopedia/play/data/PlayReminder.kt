package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
data class PlayReminder(
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