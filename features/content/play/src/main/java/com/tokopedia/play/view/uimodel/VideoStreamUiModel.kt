package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play_common.model.PlayBufferControl

/**
 * Created by jegul on 16/12/19
 */
data class VideoStreamUiModel(
        val uriString: String,
        val channelType: PlayChannelType,
        val orientation: VideoOrientation,
        val buffer: PlayBufferControl,
        val backgroundUrl: String,
        val isActive: Boolean,
        val lastMillis: Long?
)