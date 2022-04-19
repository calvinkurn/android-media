package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException


/**
 * Created by mzennis on 03/08/21.
 */
sealed class PlayLivePusherMediatorState {

    abstract val tag: String

    object Idle: PlayLivePusherMediatorState() {
        override val tag: String = IDLE
    }
    object Connecting: PlayLivePusherMediatorState() {
        override val tag: String = CONNECTING
    }
    object Started: PlayLivePusherMediatorState() {
        override val tag: String = STARTED

    }
    data class Resume(val isResumed: Boolean): PlayLivePusherMediatorState() {
        override val tag: String = if (this.isResumed) RESUMED else RESUME
    }

    object Recovered: PlayLivePusherMediatorState() {
        override val tag: String = RECOVERED
    }

    object Paused: PlayLivePusherMediatorState() {
        override val tag: String = PAUSED
    }

    object Stopped: PlayLivePusherMediatorState() {
        override val tag: String = STOPPED
    }

    data class Error(val error: PlayLivePusherException): PlayLivePusherMediatorState() {
        override val tag: String = "$ERROR: reason:${this.error.reason}"
    }

    companion object {
        private const val IDLE = "IDLE"
        private const val CONNECTING = "CONNECTING"
        private const val STARTED = "STARTED"
        private const val RESUME = "RESUME"
        private const val RESUMED = "RESUMED"
        private const val RECOVERED = "RECOVERED"
        private const val PAUSED = "PAUSED"
        private const val STOPPED = "STOPPED"
        private const val ERROR = "ERROR"
    }
}