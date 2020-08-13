package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 24/05/20.
 */
enum class PlayChannelStatus(val value: String) {
    Deleted("-1"),
    Draft("0"),
    Active("1"),
    Live("2"),
    Pause("3"),
    Stop("4"),
    Unknown("-2");

    companion object {

        private val values = values()

        fun getByValue(value: String): PlayChannelStatus {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return Unknown
        }
    }
}