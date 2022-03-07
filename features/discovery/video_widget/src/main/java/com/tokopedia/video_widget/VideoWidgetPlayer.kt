package com.tokopedia.video_widget

import kotlinx.coroutines.flow.Flow

interface VideoWidgetPlayer {
    val hasVideo: Boolean
    fun playVideo(): Flow<VideoPlayerState>
    fun stopVideo()
}