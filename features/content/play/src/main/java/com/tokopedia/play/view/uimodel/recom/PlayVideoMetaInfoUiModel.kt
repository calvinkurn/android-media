package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 22/01/21
 */
data class PlayVideoMetaInfoUiModel(
        val videoPlayer: PlayVideoPlayerUiModel,
        val videoStream: PlayVideoStreamUiModel
)

data class PlayVideoConfigUiModel(
        val id: String = "",
        val orientation: VideoOrientation = VideoOrientation.Unknown,
)