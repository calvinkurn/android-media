package com.tokopedia.play.model

import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardWrapperUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayChannelDataModelBuilder {

    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()
    private val likeModelBuilder = PlayLikeModelBuilder()
    private val channelReportBuilder = PlayChannelReportModelBuilder()
    private val pinnedBuilder = PlayPinnedModelBuilder()
    private val videoInfoBuilder = PlayVideoModelBuilder()
    private val upcomingInfoBuilder = PlayUpcomingInfoModelBuilder()

    private val uiModelBuilder = UiModelBuilder.get()

    fun buildChannelData(
        id: String = "1",
        channelDetail: PlayChannelDetailUiModel = channelInfoBuilder.buildChannelDetail(),
        partnerInfo: PlayPartnerInfo = partnerInfoBuilder.buildPlayPartnerInfo(),
        likeInfo: PlayLikeInfoUiModel = likeModelBuilder.buildLikeInfo(),
        channelReportInfo: PlayChannelReportUiModel = channelReportBuilder.buildChannelReport(),
        pinnedInfo: PlayPinnedInfoUiModel = pinnedBuilder.buildInfo(),
        quickReplyInfo: PlayQuickReplyInfoUiModel = uiModelBuilder.buildQuickReply(),
        videoMetaInfo: PlayVideoMetaInfoUiModel = videoInfoBuilder.buildVideoMeta(),
        status: PlayStatusUiModel = uiModelBuilder.buildStatus(),
        leaderboardInfo: PlayLeaderboardWrapperUiModel = PlayLeaderboardWrapperUiModel.Unknown,
        upcomingInfo: PlayUpcomingUiModel = upcomingInfoBuilder.buildUpcomingInfo(),
        tagItems: TagItemUiModel = uiModelBuilder.buildTagItem()
    ) = PlayChannelData(
        id = id,
        channelDetail = channelDetail,
        partnerInfo = partnerInfo,
        likeInfo = likeInfo,
        pinnedInfo = pinnedInfo,
        quickReplyInfo = quickReplyInfo,
        videoMetaInfo = videoMetaInfo,
        status = status,
        leaderboardInfo = leaderboardInfo,
        channelReportInfo = channelReportInfo,
        upcomingInfo = upcomingInfo,
        tagItems = tagItems,
    )
}