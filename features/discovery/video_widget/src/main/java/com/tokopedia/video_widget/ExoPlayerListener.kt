package com.tokopedia.video_widget

interface ExoPlayerListener {
    fun onPlayerIdle()
    fun onPlayerBuffering()
    fun onPlayerPlaying()
    fun onPlayerPaused()
    fun onPlayerEnded()
    fun onPlayerError(errorString: String?)
}