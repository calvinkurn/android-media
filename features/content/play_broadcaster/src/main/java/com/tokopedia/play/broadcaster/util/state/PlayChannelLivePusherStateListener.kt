package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorListener
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherState
import com.tokopedia.play_common.types.PlayChannelStatusType


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayChannelLivePusherStateListener : PlayLivePusherMediatorListener {

    override fun onLivePusherStateChanged(state: PlayLivePusherState) {
        when(state) {
            PlayLivePusherState.Started,
            PlayLivePusherState.Resumed,
            PlayLivePusherState.Recovered-> onChannelStateChanged(PlayChannelStatusType.Live)
            PlayLivePusherState.Pause -> onChannelStateChanged(PlayChannelStatusType.Pause)
            PlayLivePusherState.Stop -> onChannelStateChanged(PlayChannelStatusType.Stop)
        }
    }

    fun onChannelStateChanged(channelStatusType: PlayChannelStatusType)
}
