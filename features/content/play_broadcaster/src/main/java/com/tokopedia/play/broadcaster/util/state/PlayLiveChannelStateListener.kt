package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorListener
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorState
import com.tokopedia.play_common.types.PlayChannelStatusType


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayLiveChannelStateListener : PlayLivePusherMediatorListener {

    override fun onLivePusherStateChanged(state: PlayLivePusherMediatorState) {
        when(state) {
            PlayLivePusherMediatorState.Started,
            PlayLivePusherMediatorState.Recovered-> onChannelStateChanged(PlayChannelStatusType.Live)
            is PlayLivePusherMediatorState.Resume -> if (state.isResumed) onChannelStateChanged(PlayChannelStatusType.Live)
            PlayLivePusherMediatorState.Paused -> onChannelStateChanged(PlayChannelStatusType.Pause)
            PlayLivePusherMediatorState.Stopped -> onChannelStateChanged(PlayChannelStatusType.Stop)
            else -> {
                // ignore
            }
        }
    }

    fun onChannelStateChanged(channelStatusType: PlayChannelStatusType)
}
