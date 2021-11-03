package com.tokopedia.play.view.uimodel.state

import androidx.annotation.StringRes
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play_common.model.ui.PlayLeaderboardWrapperUiModel

/**
 * Created by jegul on 28/06/21
 */
data class PlayViewerNewUiState(
    val interactiveView: PlayInteractiveViewUiState,
    val partner: PlayPartnerUiState,
    val winnerBadge: PlayWinnerBadgeUiState,
    val bottomInsets: Map<BottomInsetsType, BottomInsetsState>,
    val like: PlayLikeUiState,
    val totalView: PlayTotalViewUiState,
    val share: PlayShareUiState,
    val cart: PlayCartUiState,
    val rtn: PlayRtnUiState,
)

data class PlayInteractiveViewUiState(
    val interactive: PlayInteractiveUiState,
    val visibility: ViewVisibility,
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

data class PlayPartnerUiState(
    val name: String,
    val followStatus: PlayPartnerFollowStatus,
)

data class PlayWinnerBadgeUiState(
    val leaderboards: PlayLeaderboardWrapperUiModel,
    val shouldShow: Boolean
)

enum class PlayLikeMode {
    Single,
    Multiple,
    Unknown,
}

data class PlayLikeUiState(
        val shouldShow: Boolean,
        val canLike: Boolean,
        val totalLike: String,
        val likeMode: PlayLikeMode,
        val isLiked: Boolean,
        val canShowBubble: Boolean,
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

data class PlayShareUiState(
    val shouldShow: Boolean
)

data class PlayTotalViewUiState(
    val viewCountStr: String
)

enum class ViewVisibility {

    Visible,
    Invisible,
    Gone
}