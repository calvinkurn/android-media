package com.tokopedia.play.broadcaster.view.state

import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayLivePusherViewState {
    object Connecting: PlayLivePusherViewState()
    object Started: PlayLivePusherViewState()
    data class Resume(val isResumed: Boolean): PlayLivePusherViewState()
    object Recovered: PlayLivePusherViewState()
    object Paused: PlayLivePusherViewState()
    data class Stopped(val shouldNavigate: Boolean): PlayLivePusherViewState()
    data class Error(val error: PlayLivePusherException): PlayLivePusherViewState()
}