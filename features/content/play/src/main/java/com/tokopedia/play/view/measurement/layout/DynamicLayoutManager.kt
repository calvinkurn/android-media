package com.tokopedia.play.view.measurement.layout

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 04/08/20
 */
interface DynamicLayoutManager {

    fun onVideoOrientationChanged(videoOrientation: VideoOrientation)

    fun onVideoPlayerChanged(videoPlayerUiModel: VideoPlayerUiModel, channelType: PlayChannelType)
}