package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 23/07/21
 */
data class PlayChannelDetailUiModel(
        val shareInfo: PlayShareInfoUiModel = PlayShareInfoUiModel(),
        val channelInfo: PlayChannelInfoUiModel = PlayChannelInfoUiModel(),
)