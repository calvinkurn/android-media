package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException


/**
 * Created by mzennis on 03/08/21.
 */
sealed class PlayLivePusherMediatorState {
    object Idle: PlayLivePusherMediatorState()
    object Connecting: PlayLivePusherMediatorState()
    object Started: PlayLivePusherMediatorState()
    data class Resume(val isResumed: Boolean): PlayLivePusherMediatorState()
    object Recovered: PlayLivePusherMediatorState()
    object Paused: PlayLivePusherMediatorState()
    object Stopped: PlayLivePusherMediatorState()
    data class Error(val error: PlayLivePusherException): PlayLivePusherMediatorState()
}