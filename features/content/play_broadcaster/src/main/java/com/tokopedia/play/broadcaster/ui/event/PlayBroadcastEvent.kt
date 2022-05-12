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

    object ShowQuizDetailBottomSheet : PlayBroadcastEvent
    data class ShowQuizDetailError(val error: Throwable) : PlayBroadcastEvent

    sealed interface CreateInteractive : PlayBroadcastEvent {
        data class Success(val durationInMs: Long) : CreateInteractive
        data class Error(val error: Throwable) : CreateInteractive
    }
}