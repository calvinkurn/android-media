package com.tokopedia.play.model.channel

import com.tokopedia.play.model.status.ChannelStatusBuilder
import com.tokopedia.play.model.tagitem.TagItemBuilder
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.recom.ExploreWidgetConfig
import com.tokopedia.play.view.uimodel.recom.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelRecommendationConfig
import com.tokopedia.play.view.uimodel.recom.PlayChannelReportUiModel
import com.tokopedia.play.view.uimodel.recom.PlayEmptyBottomSheetInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.PlayPinnedInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPopUpConfigUiModel
import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayShareInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoConfigUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play.view.uimodel.recom.realtimenotif.PlayRealTimeNotificationConfig
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
class ChannelBuilderImpl(
    private val tagItemBuilder: TagItemBuilder,
    private val statusBuilder: ChannelStatusBuilder,
) : ChannelBuilder,
    TagItemBuilder by tagItemBuilder,
    ChannelStatusBuilder by statusBuilder {

    override fun buildChannelData(
        id: String,
        channelDetail: PlayChannelDetailUiModel,
        partnerInfo: PlayPartnerInfo,
        likeInfo: PlayLikeInfoUiModel,
        channelReportInfo: PlayChannelReportUiModel,
        pinnedInfo: PlayPinnedInfoUiModel,
        quickReplyInfo: PlayQuickReplyInfoUiModel,
        videoMetaInfo: PlayVideoMetaInfoUiModel,
        leaderboard: LeaderboardUiModel,
        upcomingInfo: PlayUpcomingUiModel,
        tagItems: TagItemUiModel,
        status: PlayStatusUiModel
    ) = PlayChannelData(
        id = id,
        channelDetail = channelDetail,
        partnerInfo = partnerInfo,
        likeInfo = likeInfo,
        channelReportInfo = channelReportInfo,
        pinnedInfo = pinnedInfo,
        quickReplyInfo = quickReplyInfo,
        videoMetaInfo = videoMetaInfo,
        leaderboard = leaderboard,
        upcomingInfo = upcomingInfo,
        tagItems = tagItems,
        status = status,
    )

    override fun buildChannelDetail(
        shareInfo: PlayShareInfoUiModel,
        channelInfo: PlayChannelInfoUiModel,
        rtnConfigInfo: PlayRealTimeNotificationConfig,
        videoInfo: PlayVideoConfigUiModel,
        emptyBottomSheetInfo: PlayEmptyBottomSheetInfoUiModel,
        bottomSheetTitle: String,
        popupConfig: PlayPopUpConfigUiModel,
        channelRecomConfig: PlayChannelRecommendationConfig,
        showCart: Boolean
    ) = PlayChannelDetailUiModel(
        shareInfo = shareInfo,
        channelInfo = channelInfo,
        rtnConfigInfo = rtnConfigInfo,
        videoInfo = videoInfo,
        emptyBottomSheetInfo = emptyBottomSheetInfo,
        bottomSheetTitle = bottomSheetTitle,
        popupConfig = popupConfig,
        channelRecomConfig = channelRecomConfig,
        showCart = showCart,
    )

    override fun buildPinnedMessage(
        id: String,
        appLink: String,
        title: String
    ) = PinnedMessageUiModel(
        id = id,
        appLink = appLink,
        title = title,
    )

    override fun buildVideoStream(
        id: String,
        orientation: VideoOrientation,
        title: String
    ) = PlayVideoStreamUiModel(
        id = id,
        orientation = orientation,
        title = title,
    )
}
