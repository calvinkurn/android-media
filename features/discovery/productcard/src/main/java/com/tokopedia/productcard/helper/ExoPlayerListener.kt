package com.tokopedia.productcard.helper

interface ExoPlayerListener {
    fun onPlayerIdle()
    fun onPlayerBuffering()
    fun onPlayerPlaying()
    fun onPlayerPaused()
    fun onPlayerEnded()
    fun onPlayerError(errorString: String?)
}