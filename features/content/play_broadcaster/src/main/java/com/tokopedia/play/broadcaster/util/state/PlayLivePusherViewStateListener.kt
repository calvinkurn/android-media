package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorListener
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherState
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherViewState


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayLivePusherViewStateListener : PlayLivePusherMediatorListener {

    override fun onLivePusherStateChanged(state: PlayLivePusherState) {
        when(state) {
            PlayLivePusherState.Connecting -> onLivePusherViewStateChanged(PlayLivePusherViewState.Connecting)
            PlayLivePusherState.Pause -> onLivePusherViewStateChanged(PlayLivePusherViewState.Paused)
            PlayLivePusherState.Started -> onLivePusherViewStateChanged(PlayLivePusherViewState.Started)
            PlayLivePusherState.Resumed -> onLivePusherViewStateChanged(PlayLivePusherViewState.Resume(isResumed = true))
            PlayLivePusherState.Recovered -> onLivePusherViewStateChanged(PlayLivePusherViewState.Recovered)
            is PlayLivePusherState.Error -> onLivePusherViewStateChanged(PlayLivePusherViewState.Error(PlayLivePusherException(state.reason)))
        }
    }

    override fun onShouldContinueLiveStreaming() {
        onLivePusherViewStateChanged(PlayLivePusherViewState.Resume(isResumed = false))
    }

    fun onLivePusherViewStateChanged(viewState: PlayLivePusherViewState)
}
