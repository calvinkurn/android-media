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
        - Endpoint malformed, should be: rtmp://ip:port/appname/streamname
        - Handshake failed
        - description
        - onStatus $code
        - authentication rtmp error

        network poor:
        - Error send packet, Software caused connection abort

        network loss:
        - Error configure stream, broadcast.tokopedia.com

        system error:
        - Error preparing stream, This device cant do it
         */
        fun errorStateParser(reason: String): PlayLivePusherErrorType {
            return when {
                reason.contains("error preparing stream, this device cant do it", true) -> PlayLivePusherErrorType.SystemError
                reason.contains("error send packet", true) -> PlayLivePusherErrorType.NetworkPoor
                reason.contains("error configure stream", true) -> PlayLivePusherErrorType.NetworkLoss
                else -> PlayLivePusherErrorType.ConnectFailed
            }
        }
    }
}
