package com.tokopedia.play.view.uimodel.state

import androidx.annotation.StringRes
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel

/**
 * Created by jegul on 28/06/21
 */
data class PlayViewerNewUiState(
        val interactive: PlayInteractiveUiState = PlayInteractiveUiState.NoInteractive,
        val followStatus: PlayPartnerFollowStatus = PlayPartnerFollowStatus.NotFollowable,
        val partnerName: String = "",
        val leaderboard: PlayLeaderboardUiState = PlayLeaderboardUiState(),
        val bottomInsets: Map<BottomInsetsType, BottomInsetsState> = emptyMap(),
        val status: PlayStatusType = PlayStatusType.Active,
)

sealed class PlayInteractiveUiState {

    object NoInteractive : PlayInteractiveUiState()

    data class PreStart(
            val timeToStartInMs: Long,
            val title: String,
    ) : PlayInteractiveUiState()

    data class Ongoing(
            val timeRemainingInMs: Long,
    ) : PlayInteractiveUiState()

    data class Finished(
            @StringRes val info: Int,
    ) : PlayInteractiveUiState()
}

data class PlayLeaderboardUiState(
        val showBadge: Boolean = false,
        val winnerList: List<PlayLeaderboardUiModel> = emptyList()
)