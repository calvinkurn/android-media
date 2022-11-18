package com.tokopedia.play.model.channel

import com.tokopedia.play.model.status.ChannelStatusBuilder
import com.tokopedia.play.model.tagitem.TagItemBuilder
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.recom.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelReportUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.PlayPinnedInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
interface ChannelBuilder : TagItemBuilder, ChannelStatusBuilder {

    fun buildChannelData(
        id: String = "1",
        channelDetail: PlayChannelDetailUiModel = PlayChannelDetailUiModel(),
        partnerInfo: PlayPartnerInfo = PlayPartnerInfo(),
        likeInfo: PlayLikeInfoUiModel = PlayLikeInfoUiModel(),
        channelReportInfo: PlayChannelReportUiModel = PlayChannelReportUiModel(),
        pinnedInfo: PlayPinnedInfoUiModel = PlayPinnedInfoUiModel(buildPinnedMessage()),
        quickReplyInfo: PlayQuickReplyInfoUiModel = PlayQuickReplyInfoUiModel(emptyList()),
        videoMetaInfo: PlayVideoMetaInfoUiModel = PlayVideoMetaInfoUiModel(
            PlayVideoPlayerUiModel.Unknown,
            buildVideoStream()
        ),
        leaderboard: LeaderboardUiModel = LeaderboardUiModel.Empty,
        upcomingInfo: PlayUpcomingUiModel = PlayUpcomingUiModel(),
        tagItems: TagItemUiModel = buildTagItem(),
        status: PlayStatusUiModel = buildStatus(),
    ): PlayChannelData

    fun buildPinnedMessage(
        id: String = "1",
        appLink: String = "",
        title: String = "Pinned 1",
    ): PinnedMessageUiModel

    fun buildVideoStream(
        id: String = "1",
        orientation: VideoOrientation = VideoOrientation.Vertical,
        title: String = "Video 1"
    ): PlayVideoStreamUiModel
}