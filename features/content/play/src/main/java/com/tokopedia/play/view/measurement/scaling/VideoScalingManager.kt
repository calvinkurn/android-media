package com.tokopedia.play.view.measurement.scaling

import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 05/08/20
 */
interface VideoScalingManager {

    fun onBottomInsetsShown(bottomMostBounds: Int, videoPlayer: VideoPlayerUiModel, videoOrientation: VideoOrientation)

    fun onBottomInsetsHidden(videoPlayer: VideoPlayerUiModel)

    fun onDestroy()
}