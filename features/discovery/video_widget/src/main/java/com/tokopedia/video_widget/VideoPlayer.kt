package com.tokopedia.video_widget

import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.flow.Flow

interface VideoPlayer {
    val hasVideo: Boolean
    fun playVideo(exoPlayer: ExoPlayer): Flow<VideoPlayerState>
    fun stopVideo()
}
