package com.tokopedia.productcard.video

import kotlinx.coroutines.flow.Flow

interface ProductVideoPlayer {
    val hasProductVideo: Boolean
    fun playVideo(): Flow<VideoPlayerState>
    fun stopVideo()
}