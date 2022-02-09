package com.tokopedia.play.view.type

/**
 * Created by jegul on 16/12/19
 */
enum class PlayChannelType(val value: String) {
    Live("live"),
    VOD("vod"),
    Upcoming("upcoming"),
    Unknown("unknown");

    val isLive: Boolean
        get() = this == Live

    val isVod: Boolean
        get() = this == VOD

    val isUpcoming: Boolean
        get() = this == Upcoming

    companion object {
        private val values = values()

        fun getByValue(value: String): PlayChannelType {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return Unknown
        }
    }
}