package com.tokopedia.play.broadcaster.ui.event

sealed interface PlayBroadcastEvent {

    data class ShowError(
        val error: Throwable
    ) : PlayBroadcastEvent

    data class ShowScheduleError(
        val error: Throwable
    ) : PlayBroadcastEvent

    data class SetScheduleSuccess(val isEdit: Boolean) : PlayBroadcastEvent
    object DeleteScheduleSuccess : PlayBroadcastEvent

    data class ShowErrorCreateQuiz(
        val error: Throwable
    ) : PlayBroadcastEvent
}