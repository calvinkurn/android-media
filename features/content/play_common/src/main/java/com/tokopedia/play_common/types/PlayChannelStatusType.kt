package com.tokopedia.play_common.types


/**
 * Created by mzennis on 24/05/20.
 * https://tokopedia.atlassian.net/wiki/spaces/CN/pages/844006102/Broadcaster+Constants+-+Status+Types
 */
enum class PlayChannelStatusType(val value: String) {
    Draft("0"),
    Live("2"),
    Pause("3"),
    Stop("4"),
    Moderated("5"),
    Deleted("-1"),
    Transcoding("6"),
    ScheduledLive("8"),
    Unknown("-2");

    companion object {

        private val values = values()

        fun getByValue(value: String): PlayChannelStatusType {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return Unknown
        }
    }
}