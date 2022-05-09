package com.tokopedia.play.view.uimodel.state

import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play.view.uimodel.recom.interactive.InteractiveStateUiModel
import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VariantUiModel
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created by jegul on 28/06/21
 */
data class PlayViewerNewUiState(
    val channel: PlayChannelDetailUiModel,
    val interactive: InteractiveStateUiModel,
    val partner: PlayPartnerInfo,
    val winnerBadge: PlayWinnerBadgeUiState,
    val bottomInsets: Map<BottomInsetsType, BottomInsetsState>,
    val like: PlayLikeUiState,
    val totalView: PlayTotalViewUiState,
    val rtn: PlayRtnUiState,
    val title: PlayTitleUiState,
    val tagItems: TagItemUiModel,
    val status: PlayStatusUiModel,
    val quickReply: PlayQuickReplyInfoUiModel,
    val kebabMenu: PlayKebabMenuUiState,
    val selectedVariant: NetworkResult<VariantUiModel>,
    val isLoadingBuy: Boolean,
)

data class PlayWinnerBadgeUiState(
    val leaderboards: LeaderboardUiModel,
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

data class PlayTotalViewUiState(
    val viewCountStr: String
)

data class PlayTitleUiState(
    val title: String
)

data class PlayKebabMenuUiState(
    val shouldShow: Boolean
)

enum class KebabMenuType{
    ThreeDots,
    UserReportList,
    UserReportSubmission
}

