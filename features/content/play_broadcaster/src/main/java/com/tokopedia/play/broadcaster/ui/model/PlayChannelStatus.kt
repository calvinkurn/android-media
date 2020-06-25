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

        fun getChannelStatus(
                activeLiveChannel: Int,
                pausedChannel: Int,
                draftChannel: Int
        ): Pair<String, PlayChannelStatus> {
            var channelId = 0
            var playChannelStatus = Unknown
            when {
                activeLiveChannel > 0 -> {
                    channelId = activeLiveChannel
                    playChannelStatus = Live
                }
                pausedChannel > 0 -> {
                    channelId = pausedChannel
                    playChannelStatus = Pause
                }
                draftChannel > 0 -> {
                    channelId = draftChannel
                    playChannelStatus = Draft
                }
            }
            return Pair(channelId.toString(), playChannelStatus)
        }
    }
}