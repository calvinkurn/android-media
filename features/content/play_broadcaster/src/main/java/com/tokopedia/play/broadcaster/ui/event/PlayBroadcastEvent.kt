package com.tokopedia.play.broadcaster.ui.event

sealed interface PlayBroadcastEvent {

    object ShowLoading : PlayBroadcastEvent
    object ShowResumeLiveDialog : PlayBroadcastEvent
    object ShowLiveEndedDialog : PlayBroadcastEvent

    data class ShowError(
        val error: Throwable,
        val onRetry: (() -> Unit)? = null,
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
    data class ShowQuizDetailBottomSheetError(val error: Throwable) : PlayBroadcastEvent

    object ShowLeaderboardBottomSheet : PlayBroadcastEvent
    data class ShowLeaderboardBottomSheetError(val error: Throwable) : PlayBroadcastEvent

    sealed interface CreateInteractive : PlayBroadcastEvent {
        data class Success(val durationInMs: Long) : CreateInteractive
        data class Error(val error: Throwable) : CreateInteractive
    }

    data class ShowInteractiveGameResultWidget(val showCoachMark: Boolean): PlayBroadcastEvent
    object DismissGameResultCoachMark : PlayBroadcastEvent

    data class FailPinUnPinProduct(val throwable: Throwable, val isPinned: Boolean): PlayBroadcastEvent
    object BroadcastStarted : PlayBroadcastEvent
    data class BroadcastReady(val ingestUrl: String) : PlayBroadcastEvent
    data class ShowBroadcastError(
        val error: Throwable,
    ) : PlayBroadcastEvent
    object BroadcastRecovered : PlayBroadcastEvent
}