package com.tokopedia.play.view.measurement.bounds.manager

import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 05/08/20
 */
interface VideoBoundsManager {

    fun invalidateVideoBounds(
            videoOrientation: VideoOrientation,
            videoPlayer: VideoPlayerUiModel,
            topBounds: Int
    )
}