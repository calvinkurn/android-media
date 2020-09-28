package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 06/07/20.
 */
sealed class BroadcastState {
    object Init: BroadcastState()
    object Start: BroadcastState()
    object Pause: BroadcastState()
    data class Stop(val shouldNavigate: Boolean): BroadcastState()
    data class Error(val error: Throwable, val onRetry: () -> Unit): BroadcastState()
}