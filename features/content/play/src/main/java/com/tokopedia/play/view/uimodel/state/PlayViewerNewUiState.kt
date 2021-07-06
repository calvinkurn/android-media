package com.tokopedia.play.view.uimodel.state

import androidx.annotation.StringRes
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel

/**
 * Created by jegul on 28/06/21
 */
data class PlayViewerNewUiState(
        val interactive: PlayInteractiveUiState = PlayInteractiveUiState.NoInteractive,
        val followStatus: PlayFollowStatusUiState = PlayFollowStatusUiState.NotFollowable,
        val partnerName: String = "",
        val showWinningBadge: Boolean = false,
        val winnerLeaderboard: List<PlayLeaderboardUiModel> = emptyList(),
        val bottomInsets: Map<BottomInsetsType, BottomInsetsState> = emptyMap()
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

sealed class PlayFollowStatusUiState {

    data class Followable(val isFollowing: Boolean) : PlayFollowStatusUiState()
    object NotFollowable : PlayFollowStatusUiState()
}