package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.view.state.PlayLivePusherState
import com.tokopedia.play_common.types.PlayChannelStatusType


/**
 * Created by mzennis on 17/03/21.
 */
interface PlayChannelLiveStateListener : PlayLiveStateListener {

    override fun onStateChanged(state: PlayLivePusherState) {
        when(state) {
            is PlayLivePusherState.Start -> onChannelStateChanged(PlayChannelStatusType.Live)
            is PlayLivePusherState.Resume -> if (state.isResumed) onChannelStateChanged(PlayChannelStatusType.Live)
            is PlayLivePusherState.Pause -> onChannelStateChanged(PlayChannelStatusType.Pause)
            is PlayLivePusherState.Stop -> onChannelStateChanged(PlayChannelStatusType.Stop)
        }
    }

    fun onChannelStateChanged(channelStatusType: PlayChannelStatusType)
}