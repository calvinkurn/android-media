package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 24/05/20.
 * https://tokopedia.atlassian.net/wiki/spaces/CN/pages/844006102/Broadcaster+Constants+-+Status+Types
 */
enum class ChannelStatus(val value: String) {
    Draft("0"),
    Live("2"),
    Pause("3"),
    Stop("4"),
    CompleteDraft("-2"),
    Unknown("-1");

    companion object {

        private val values = values()

        fun getByValue(value: String): ChannelStatus {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return Unknown
        }

        fun getChannelType(
                activeLiveChannel: Int,
                pausedChannel: Int,
                draftChannel: Int,
                completeDraft: Boolean
        ): Pair<String, ChannelStatus> {
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
                completeDraft && draftChannel > 0 -> {
                    channelId = draftChannel
                    playChannelStatus = CompleteDraft
                }
                draftChannel > 0 -> {
                    channelId = draftChannel
                    playChannelStatus = Draft
                }
            }
            return Pair(channelId.toString(), playChannelStatus)
        }
    }

    val isPause: Boolean
        get() = this == Pause

    val isLive: Boolean
        get() = this == Live
}