package com.tokopedia.play.broadcaster.ui.model.interactive

/**
 * Created by jegul on 07/07/21
 */
sealed class BroadcastInteractiveState {

    object Forbidden : BroadcastInteractiveState()
    sealed class Allowed : BroadcastInteractiveState() {
        data class Init(val state: BroadcastInteractiveInitState) : Allowed()
        data class Schedule(val timeToStartInMs: Long, val durationInMs: Long, val title: String) : Allowed()
        data class Live(val remainingTimeInMs: Long) : Allowed()
    }
}

sealed class BroadcastInteractiveInitState {

    data class NoPrevious(val showOnBoarding: Boolean) : BroadcastInteractiveInitState()
    object Loading : BroadcastInteractiveInitState()
    object HasPrevious : BroadcastInteractiveInitState()
}