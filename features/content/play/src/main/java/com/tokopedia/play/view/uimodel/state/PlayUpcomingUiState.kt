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
    val title: String,
    val coverUrl: String,
    val startTime: String
)