package com.tokopedia.play.view.measurement.scaling

import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel

/**
 * Created by jegul on 05/08/20
 */
interface VideoScalingManager {

    fun onBottomInsetsShown(bottomMostBounds: Int, videoPlayer: PlayVideoPlayerUiModel, videoOrientation: VideoOrientation)

    fun onBottomInsetsHidden(videoPlayer: PlayVideoPlayerUiModel)

    fun onDestroy()
}