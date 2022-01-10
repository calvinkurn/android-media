package com.tokopedia.play.view.uimodel.state

import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus

/**
 * Created By : Jonathan Darwin on November 15, 2021
 */
data class PlayUpcomingUiState(
    val partner: PlayUpcomingPartnerUiState,
    val upcomingInfo: PlayUpcomingInfoUiState,
    val share: PlayUpcomingShareUiState,
)

data class PlayUpcomingPartnerUiState(
    val name: String,
    val followStatus: PlayPartnerFollowStatus,
)

data class PlayUpcomingShareUiState(
    val shouldShow: Boolean
)

data class PlayUpcomingInfoUiState(
    val generalInfo: PlayUpcomingGeneralInfo,
    val state: PlayUpcomingState
)

data class PlayUpcomingGeneralInfo(
    val title: String,
    val coverUrl: String,
    val startTime: String,
    val waitingDuration: Int,
)

sealed class PlayUpcomingState {
    object Unknown: PlayUpcomingState()

    object RemindMe: PlayUpcomingState()
    object Reminded: PlayUpcomingState()
    object WatchNow: PlayUpcomingState()
    object WaitingRefreshDuration: PlayUpcomingState()
    object Refresh: PlayUpcomingState()
    object Loading: PlayUpcomingState()
}