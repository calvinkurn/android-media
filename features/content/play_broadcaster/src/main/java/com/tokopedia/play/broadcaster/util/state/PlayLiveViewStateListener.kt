package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorListener
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorState
import com.tokopedia.play.broadcaster.view.state.PlayLiveViewState


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayLiveViewStateListener : PlayLivePusherMediatorListener {

    override fun onLivePusherStateChanged(state: PlayLivePusherMediatorState) {
        when (state) {
            PlayLivePusherMediatorState.Connecting -> onLivePusherViewStateChanged(PlayLiveViewState.Connecting)
            PlayLivePusherMediatorState.Paused -> onLivePusherViewStateChanged(PlayLiveViewState.Paused)
            PlayLivePusherMediatorState.Started -> onLivePusherViewStateChanged(PlayLiveViewState.Started)
            is PlayLivePusherMediatorState.Resume -> onLivePusherViewStateChanged(PlayLiveViewState.Resume(state.isResumed))
            PlayLivePusherMediatorState.Recovered -> onLivePusherViewStateChanged(PlayLiveViewState.Recovered)
            is PlayLivePusherMediatorState.Error -> onLivePusherViewStateChanged(PlayLiveViewState.Error(state.error))
            else -> {
                // ignore
            }
        }
    }

    fun onLivePusherViewStateChanged(viewState: PlayLiveViewState)
}
