package com.tokopedia.play.view.uimodel

/**
 * Created by jegul on 18/12/19
 */
data class PlayCompleteInfoUiModel(
        val channelInfo: ChannelInfoUiModel,
        val videoStream: VideoStreamUiModel,
        val videoPlayer: VideoPlayerUiModel,
        val totalView: TotalViewUiModel,
        val pinnedMessage: PinnedMessageUiModel?,
        val pinnedProduct: PinnedProductUiModel?,
        val quickReply: QuickReplyUiModel,
        val event: EventUiModel
)