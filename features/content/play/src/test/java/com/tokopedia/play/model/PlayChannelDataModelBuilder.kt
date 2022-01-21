package com.tokopedia.play.model

import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play_common.model.ui.PlayLeaderboardWrapperUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayChannelDataModelBuilder {

    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()
    private val likeModelBuilder = PlayLikeModelBuilder()
    private val channelReportBuilder = PlayChannelReportModelBuilder()
    private val cartInfoBuilder = PlayCartInfoModelBuilder()
    private val pinnedBuilder = PlayPinnedModelBuilder()
    private val quickReplyBuilder = PlayQuickReplyModelBuilder()
    private val videoInfoBuilder = PlayVideoModelBuilder()
    private val statusInfoBuilder = PlayStatusInfoModelBuilder()
    private val upcomingInfoBuilder = PlayUpcomingInfoModelBuilder()

    fun buildChannelData(
        id: String = "1",
        channelDetail: PlayChannelDetailUiModel = channelInfoBuilder.buildChannelDetail(),
        partnerInfo: PlayPartnerInfo = partnerInfoBuilder.buildPlayPartnerInfo(),
        likeInfo: PlayLikeInfoUiModel = likeModelBuilder.buildLikeInfo(),
        channelReportInfo: PlayChannelReportUiModel = channelReportBuilder.buildChannelReport(),
        pinnedInfo: PlayPinnedInfoUiModel = pinnedBuilder.buildInfo(),
        quickReplyInfo: PlayQuickReplyInfoUiModel = quickReplyBuilder.build(),
        videoMetaInfo: PlayVideoMetaInfoUiModel = videoInfoBuilder.buildVideoMeta(),
        statusInfo: PlayStatusInfoUiModel = statusInfoBuilder.build(),
        leaderboardInfo: PlayLeaderboardWrapperUiModel = PlayLeaderboardWrapperUiModel.Unknown,
        upcomingInfo: PlayUpcomingUiModel = upcomingInfoBuilder.buildUpcomingInfo()
    ) = PlayChannelData(
            id = id,
            channelDetail = channelDetail,
            partnerInfo = partnerInfo,
            likeInfo = likeInfo,
            pinnedInfo = pinnedInfo,
            quickReplyInfo = quickReplyInfo,
            videoMetaInfo = videoMetaInfo,
            statusInfo = statusInfo,
            leaderboardInfo = leaderboardInfo,
            channelReportInfo = channelReportInfo,
            upcomingInfo = upcomingInfo
    )
}