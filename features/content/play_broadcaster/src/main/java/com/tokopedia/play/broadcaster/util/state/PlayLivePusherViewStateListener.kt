package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorListener
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherState
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherErrorType
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
            is PlayLivePusherState.Error -> onLivePusherViewStateChanged(
                PlayLivePusherViewState.Error(errorStateParser(state.reason), state.reason)
            )
        }
    }

    override fun onShouldContinueLiveStreaming() {
        onLivePusherViewStateChanged(PlayLivePusherViewState.Resume(isResumed = false))
    }

    fun onLivePusherViewStateChanged(viewState: PlayLivePusherViewState)

    companion object {

        /**
         * connect failed
        - can not connect to server

        network poor / loss:
        - connection failure

        system error:
        - error preparing stream, This device cant do it
        - Video encoding failure, try to change video resolution
        - Video capture failure
        - Audio encoding failure
        - Audio capture failure
         */
        fun errorStateParser(reason: String): PlayLivePusherErrorType {
            return when {
                reason.contains("can not connect to server", true) -> PlayLivePusherErrorType.ConnectFailed
                reason.contains("connection failure", true) -> PlayLivePusherErrorType.NetworkLoss
                else -> PlayLivePusherErrorType.SystemError
            }
        }
    }
}
