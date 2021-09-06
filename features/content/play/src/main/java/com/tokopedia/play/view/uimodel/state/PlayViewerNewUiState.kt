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

        val like: PlayLikeUiState,

        val totalView: String,

        val isShareable: Boolean,

        val cart: PlayCartUiState,

        val rtn: PlayRtnUiState,
)

sealed class PlayInteractiveUiState {

    object NoInteractive : PlayInteractiveUiState()

    object Loading : PlayInteractiveUiState()

    object Error : PlayInteractiveUiState()

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

enum class PlayLikeMode {
    Single,
    Multiple
}

data class PlayLikeUiState(
        val isLiked: Boolean,
        val shouldShow: Boolean,
        val canLike: Boolean,
        val animate: Boolean,
        val totalLike: String,
        val likeMode: PlayLikeMode,
)

data class PlayCartUiState(
        val shouldShow: Boolean,
        val count: PlayCartCount,
)

sealed class PlayCartCount {

    data class Show(val countText: String) : PlayCartCount()
    object Hide : PlayCartCount()
}

data class PlayRtnUiState(
        val shouldShow: Boolean,
        val lifespanInMs: Long,
)

enum class ViewVisibility {

    Visible,
    Invisible,
    Gone
}