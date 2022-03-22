package com.tokopedia.productcard.video

interface ExoPlayerListener {
    fun onPlayerIdle()
    fun onPlayerBuffering()
    fun onPlayerPlaying()
    fun onPlayerPaused()
    fun onPlayerEnded()
    fun onPlayerError(errorString: String?)
}