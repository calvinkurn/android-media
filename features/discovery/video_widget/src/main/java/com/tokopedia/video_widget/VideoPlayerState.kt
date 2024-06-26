package com.tokopedia.video_widget

sealed interface VideoPlayerState {
    object NoVideo: VideoPlayerState
    object Starting: VideoPlayerState
    object Buffering : VideoPlayerState
    object Playing : VideoPlayerState
    object Paused : VideoPlayerState
    object Ended : VideoPlayerState
    data class Error(
        val errorMessage : String
    ) : VideoPlayerState
}