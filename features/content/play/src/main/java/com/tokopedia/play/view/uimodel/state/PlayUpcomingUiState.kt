package com.tokopedia.play.view.uimodel.state

/**
 * Created By : Jonathan Darwin on November 15, 2021
 */
data class PlayUpcomingUiState(
    val partner: PlayPartnerUiState,
    val upcomingInfo: PlayUpcomingInfoUiState,
    val share: PlayShareUiState,
)

data class PlayUpcomingInfoUiState(
    val generalInfo: PlayUpcomingGeneralInfo,
    val state: PlayUpcomingState
)

data class PlayUpcomingGeneralInfo(
    val title: String,
    val coverUrl: String,
    val startTime: String,
    val waitingDuration: Long,
)

sealed class PlayUpcomingState {
    object Unknown: PlayUpcomingState()
    object RemindMe: PlayUpcomingState()
    object Reminded: PlayUpcomingState()
    object WatchNow: PlayUpcomingState()
    object Refresh: PlayUpcomingState()
}