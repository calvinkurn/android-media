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
    val rtn: PlayRtnUiState,
    val title: PlayTitleUiState,
    val viewAllProduct: PlayViewAllProductUiState,
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
    val iconUrl: String,
    val badgeUrl: String,
    val isLoadingFollow: Boolean,
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

data class PlayTitleUiState(
    val title: String
)

data class PlayViewAllProductUiState(
    val shouldShow: Boolean
)

enum class ViewVisibility {

    Visible,
    Invisible,
    Gone
}