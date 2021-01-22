package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.uimodel.VideoPlayerUiModel
import com.tokopedia.play.view.uimodel.VideoStreamUiModel

/**
 * Created by jegul on 22/01/21
 */
data class PlayVideoMetaInfoUiModel(
        val videoPlayer: VideoPlayerUiModel,
        val videoStream: VideoStreamUiModel? = null
)