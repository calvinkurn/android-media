package com.tokopedia.play.view.uimodel

/**
 * Created by jegul on 12/08/20
 */
data class VideoMetaUiModel(
        val videoPlayer: VideoPlayerUiModel,
        val videoStream: VideoStreamUiModel? = null
)