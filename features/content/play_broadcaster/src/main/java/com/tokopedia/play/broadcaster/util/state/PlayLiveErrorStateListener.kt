package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.view.state.PlayLivePusherErrorState
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherState


/**
 * Created by mzennis on 17/03/21.
 */
interface PlayLiveErrorStateListener : PlayLiveStateListener {

    override fun onStateChanged(state: PlayLivePusherState) {
        if (state is PlayLivePusherState.Error) {
            when(state.errorState) {
                PlayLivePusherErrorState.NetworkLoss,
                PlayLivePusherErrorState.NetworkPoor -> onError(true)
                else -> onError(false)
            }
        }
    }
    fun onError(autoReconnect: Boolean)
}