package com.tokopedia.video_widget

interface VideoPlayerProvider {
    val videoPlayer: VideoPlayer?
    val isAutoplayEnabled : Boolean
}
