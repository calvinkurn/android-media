package com.tokopedia.play.view.uimodel.state

/**
 * Created by jegul on 28/06/21
 */
data class PlayViewerNewUiState(
        val interactive: PlayInteractiveUiState = PlayInteractiveUiState.NoInteractive,
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
            val info: String,
    ) : PlayInteractiveUiState()
}