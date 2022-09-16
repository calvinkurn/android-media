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
    val description: DescriptionUiState,
)

data class PlayUpcomingInfoUiState(
    val info: PlayUpcomingUiModel,
    val state: PlayUpcomingState
)
//Could be rename as WidgetUiState when product carousel is in
data class DescriptionUiState(val isExpand: Boolean = false, val isShown: Boolean = false)

sealed class PlayUpcomingState {
    object Unknown : PlayUpcomingState()
    data class ReminderStatus(val isReminded: Boolean): PlayUpcomingState()
    object WatchNow : PlayUpcomingState()
    object WaitingRefreshDuration : PlayUpcomingState()
    object Refresh : PlayUpcomingState()
    object Loading : PlayUpcomingState()
}