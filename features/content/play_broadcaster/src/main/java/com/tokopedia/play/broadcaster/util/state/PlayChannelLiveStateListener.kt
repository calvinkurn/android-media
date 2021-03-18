package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.view.state.PlayLivePusherState
import com.tokopedia.play_common.types.PlayChannelStatusType


/**
 * Created by mzennis on 17/03/21.
 */
interface PlayChannelLiveStateListener : PlayLiveStateListener {

    override fun onStateChanged(state: PlayLivePusherState) {
        when(state) {
            is PlayLivePusherState.Started,
            is PlayLivePusherState.Resumed -> onChannelStateChanged(PlayChannelStatusType.Live)
            is PlayLivePusherState.Paused -> onChannelStateChanged(PlayChannelStatusType.Pause)
            is PlayLivePusherState.Stopped -> onChannelStateChanged(PlayChannelStatusType.Stop)
        }
    }

    fun onChannelStateChanged(channelStatusType: PlayChannelStatusType)
}