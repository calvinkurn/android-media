package com.tokopedia.play.view.uimodel.state

import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.uimodel.ExploreWidgetUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play.view.uimodel.WarehouseInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play.view.uimodel.recom.interactive.InteractiveStateUiModel
import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel

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
    val address: AddressWidgetUiState,
    val featuredProducts: List<PlayProductUiModel.Product>,
    val engagement: EngagementUiState,
    val followPopUp: Boolean,
    val exploreWidget: ExploreWidgetUiState,
    val combinedState: PlayCombinedState,
) {

    companion object {
        val Empty: PlayViewerNewUiState
            get() = PlayViewerNewUiState(
                channel = PlayChannelDetailUiModel(),
                interactive = InteractiveStateUiModel.Empty,
                partner = PlayPartnerInfo(),
                winnerBadge = PlayWinnerBadgeUiState(
                    leaderboards = LeaderboardUiModel.Empty,
                    shouldShow = false,
                ),
                bottomInsets = emptyMap(),
                like = PlayLikeUiState.Empty,
                totalView = PlayTotalViewUiState("0"),
                rtn = PlayRtnUiState(
                    shouldShow = false,
                    lifespanInMs = 0L,
                ),
                title = PlayTitleUiState(""),
                tagItems = TagItemUiModel.Empty,
                status = PlayStatusUiModel.Empty,
                quickReply = PlayQuickReplyInfoUiModel.Empty,
                address = AddressWidgetUiState(
                    shouldShow = false,
                    warehouseInfo = WarehouseInfoUiModel.Empty,
                ),
                featuredProducts = emptyList(),
                engagement = EngagementUiState.Empty,
                followPopUp = false,
                exploreWidget = ExploreWidgetUiState.Empty,
                combinedState = PlayCombinedState.Empty,
            )
    }
}

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
) {
    companion object {
        val Empty: PlayLikeUiState
            get() = PlayLikeUiState(
                shouldShow = false,
                canLike = false,
                totalLike = "0",
                likeMode = PlayLikeMode.Unknown,
                isLiked = false,
                canShowBubble = false,
            )
    }
}

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

enum class KebabMenuType{
    ThreeDots,
    UserReportList,
    UserReportSubmission
}

data class AddressWidgetUiState(
    val shouldShow: Boolean,
    val warehouseInfo: WarehouseInfoUiModel
)


data class EngagementUiState(
    val shouldShow: Boolean,
    val data: List<EngagementUiModel>,
) {
    companion object {
        val Empty: EngagementUiState get() = EngagementUiState(shouldShow = false, data = emptyList())
    }
}

data class FollowPopUpUiState(
    val shouldShow: Boolean,
    val partnerId: Long,
){
    companion object {
        val Empty: FollowPopUpUiState
            get() = FollowPopUpUiState(
                shouldShow = false,
                partnerId = 0L,
            )
    }
}

data class ExploreWidgetUiState(
    val shouldShow: Boolean,
    val data: ExploreWidgetUiModel,
) {
    companion object {
        val Empty: ExploreWidgetUiState
            get() = ExploreWidgetUiState(
                shouldShow = false,
                data = ExploreWidgetUiModel.Empty,
            )
    }
}

data class PlayCombinedState(
    val videoProperty: VideoPropertyUiModel,
    val isLoadingBuy: Boolean,
    val cartCount: Int,
) {
    companion object {
        val Empty: PlayCombinedState
            get() = PlayCombinedState(
                videoProperty = VideoPropertyUiModel.Empty,
                isLoadingBuy = false,
                cartCount = 0,
            )
    }
}
