package com.tokopedia.play.broadcaster.pusher.state

/**
 * Created by meyta.taliti on 05/04/22.
 */
sealed class PlayBroadcasterState {

    abstract val tag: String

    object Started: PlayBroadcasterState() {
        override val tag: String
            get() = STARTED

    }
    data class Resume(val startedBefore: Boolean, val shouldContinue: Boolean): PlayBroadcasterState() {
        override val tag: String
            get() = "$RESUME: startedBefore: $startedBefore, shouldContinue: $shouldContinue"

    }
    object Paused: PlayBroadcasterState() {
        override val tag: String
            get() = PAUSED

    }
    object Recovered: PlayBroadcasterState() {
        override val tag: String
            get() = RECOVERED

    }
    data class Error(val cause: Throwable): PlayBroadcasterState() {
        override val tag: String
            get() = "$ERROR, cause: ${cause.message}"

    }
    object Stopped: PlayBroadcasterState() {
        override val tag: String
            get() = STOPPED

    }

    companion object {
        private const val STARTED = "STARTED"
        private const val RESUME = "RESUME"
        private const val RECOVERED = "RECOVERED"
        private const val PAUSED = "PAUSED"
        private const val STOPPED = "STOPPED"
        private const val ERROR = "ERROR"
    }
}