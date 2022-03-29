package com.tokopedia.play.broadcaster.ui.model.interactive

import com.tokopedia.play.broadcaster.ui.model.game.GameType

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

    data class NoPrevious(val showOnBoarding: Boolean, val gameTypeList: List<GameType>) : BroadcastInteractiveInitState()
    object Loading : BroadcastInteractiveInitState()
    data class HasPrevious(val coachMark: BroadcastInteractiveCoachMark) : BroadcastInteractiveInitState()
}

sealed class BroadcastInteractiveCoachMark {

    data class HasCoachMark(val title: String, val subtitle: String) : BroadcastInteractiveCoachMark()
    object NoCoachMark : BroadcastInteractiveCoachMark()
}