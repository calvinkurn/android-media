package com.tokopedia.play.widget.ui.type


/**
 * Created by mzennis on 05/10/20.
 */
enum class PlayWidgetChannelType(val value: String) {
    Live("LIVE"),
    Vod("WATCH_AGAIN"),
    Upcoming("COMING_SOON"),
    Transcoding("TRANSCODING"),
    FailedTranscoding("TRANSCODING_FAILED"),

    Deleting("INT_DELETING"),

    Unknown("");

    companion object {
        private val values = values()

        fun getByValue(value: String): PlayWidgetChannelType {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return Unknown
        }
    }
}