package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 29/12/20
 */
data class PiPInfoUiModel(
        val channelId: String,
        val partnerId: Long?,
        val channelType: PlayChannelType,
        val videoOrientation: VideoOrientation
)