package com.tokopedia.videoplayer.view.widget

interface VideoPlayerListener {
    fun autoPlay(): Boolean
    fun onPlayerError()
    fun onLoadingChanged(isLoading: Boolean)
    fun onStateChanged(playbackState: Int)
}