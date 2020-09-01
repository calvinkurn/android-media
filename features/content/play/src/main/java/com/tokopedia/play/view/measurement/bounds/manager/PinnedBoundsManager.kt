package com.tokopedia.play.view.measurement.bounds.manager

import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 01/09/20
 */
interface PinnedBoundsManager {

    suspend fun invalidatePinnedBounds(videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel, isChatMode: Boolean)
}