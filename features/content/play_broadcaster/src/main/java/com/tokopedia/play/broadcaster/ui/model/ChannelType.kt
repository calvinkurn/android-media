package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 24/05/20.
 * https://tokopedia.atlassian.net/wiki/spaces/CN/pages/844006102/Broadcaster+Constants+-+Status+Types
 */
enum class ChannelType(val value: String) {
    Draft("0"),
    Active("1"),
    Pause("3"),
    CompleteDraft("-2"),
    Unknown("-1");

    companion object {

        private val values = values()

        fun getByValue(value: String): ChannelType {
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
        ): Pair<String, ChannelType> {
            var channelId = 0
            var playChannelStatus = Unknown
            when {
                activeLiveChannel > 0 -> {
                    channelId = activeLiveChannel
                    playChannelStatus = Active
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
}