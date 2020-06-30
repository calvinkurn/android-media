package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 24/05/20.
 */
enum class PlayChanelType(val value: String) {
    Draft("0"),
    Active("1"),
    Pause("3"),
    Unknown("-1");

    companion object {

        fun getConfigType(
                activeLiveChannel: Int,
                pausedChannel: Int,
                draftChannel: Int
        ): Pair<String, PlayChanelType> {
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
                draftChannel > 0 -> {
                    channelId = draftChannel
                    playChannelStatus = Draft
                }
            }
            return Pair(channelId.toString(), playChannelStatus)
        }
    }
}