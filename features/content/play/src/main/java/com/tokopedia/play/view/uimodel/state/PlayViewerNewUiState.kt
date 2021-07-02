package com.tokopedia.play.view.uimodel.state

import androidx.annotation.StringRes

/**
 * Created by jegul on 28/06/21
 */
data class PlayViewerNewUiState(
        val interactive: PlayInteractiveUiState = PlayInteractiveUiState.NoInteractive,
        val showInteractiveFollow: Boolean = false,
        val toolbarFollowStatus: PlayToolbarFollowUiState = PlayToolbarFollowUiState.Hide,
        val showWinningBadge: Boolean = false,
)

sealed class PlayInteractiveUiState {

    object NoInteractive : PlayInteractiveUiState()

    data class PreStart(
            val timeToStartInMs: Long,
            val title: String,
    ) : PlayInteractiveUiState()

    data class Ongoing(
            val timeRemainingInMs: Long,
            val title: String,
    ) : PlayInteractiveUiState()

    data class Finished(
            @StringRes val info: Int,
    ) : PlayInteractiveUiState()
}

sealed class PlayToolbarFollowUiState {

    data class Show(val isFollowed: Boolean) : PlayToolbarFollowUiState()
    object Hide : PlayToolbarFollowUiState()
}