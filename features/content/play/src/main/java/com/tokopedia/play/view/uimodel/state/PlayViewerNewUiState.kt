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
        val interactive: PlayInteractiveUiState,
        val showInteractive: ViewVisibility,

        val partnerName: String,
        val followStatus: PlayPartnerFollowStatus,

        val leaderboards: List<PlayLeaderboardUiModel>,
        val showWinnerBadge: Boolean,

        val bottomInsets: Map<BottomInsetsType, BottomInsetsState>,
        val status: PlayStatusType,
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

enum class ViewVisibility {

    Visible,
    Invisible,
    Gone
}