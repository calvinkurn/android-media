package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.PlaySource
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel

/**
 * Created by jegul on 29/12/20
 */
data class PiPInfoUiModel(
        val channelId: String,
        val source: PlaySource,
        val partnerId: Long?,
        val channelType: PlayChannelType,
        val videoPlayer: PlayVideoPlayerUiModel.General,
        val videoStream: PlayVideoStreamUiModel,
        val stopOnClose: Boolean,
        val pipMode: PiPMode
)