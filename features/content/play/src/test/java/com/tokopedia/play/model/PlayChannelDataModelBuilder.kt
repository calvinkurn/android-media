package com.tokopedia.play.model

import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.recom.*

/**
 * Created by jegul on 09/02/21
 */
class PlayChannelDataModelBuilder {

    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()
    private val likeModelBuilder = PlayLikeModelBuilder()
    private val totalViewBuilder = PlayTotalViewModelBuilder()
    private val shareInfoBuilder = PlayShareInfoModelBuilder()
    private val cartInfoBuilder = PlayCartInfoModelBuilder()
    private val pinnedBuilder = PlayPinnedModelBuilder()
    private val quickReplyBuilder = PlayQuickReplyModelBuilder()
    private val videoInfoBuilder = PlayVideoModelBuilder()
    private val statusInfoBuilder = PlayStatusInfoModelBuilder()

    fun buildChannelData(
            id: String = "1",
            channelInfo: PlayChannelInfoUiModel = channelInfoBuilder.buildChannelInfo(),
            partnerInfo: PlayPartnerInfoUiModel = partnerInfoBuilder.buildCompleteData(),
            likeInfo: PlayLikeInfoUiModel = likeModelBuilder.buildCompleteData(),
            totalViewInfo: PlayTotalViewUiModel = totalViewBuilder.buildCompleteData(),
            shareInfo: PlayShareInfoUiModel = shareInfoBuilder.build(),
            cartInfo: PlayCartInfoUiModel = cartInfoBuilder.buildCompleteData(),
            pinnedInfo: PlayPinnedInfoUiModel = pinnedBuilder.buildInfo(),
            quickReplyInfo: PlayQuickReplyInfoUiModel = quickReplyBuilder.build(),
            videoMetaInfo: PlayVideoMetaInfoUiModel = videoInfoBuilder.buildVideoMeta(),
            statusInfo: PlayStatusInfoUiModel = statusInfoBuilder.build()
    ) = PlayChannelData(
            id = id,
            channelInfo = channelInfo,
            partnerInfo = partnerInfo,
            likeInfo = likeInfo,
            totalViewInfo = totalViewInfo,
            shareInfo = shareInfo,
            cartInfo = cartInfo,
            pinnedInfo = pinnedInfo,
            quickReplyInfo = quickReplyInfo,
            videoMetaInfo = videoMetaInfo,
            statusInfo = statusInfo
    )
}