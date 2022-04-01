package com.tokopedia.play.view.uimodel.state

import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo

/**
 * Created By : Jonathan Darwin on November 15, 2021
 */
data class PlayUpcomingUiState(
    val partner: PlayPartnerInfo,
    val upcomingInfo: PlayUpcomingInfoUiState,
    val channel: PlayChannelDetailUiModel,
)

data class PlayUpcomingInfoUiState(
    val info: PlayUpcomingUiModel,
    val state: PlayUpcomingState
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