package com.tokopedia.home.beranda.presentation.view.helper

interface ExoPlayerListener {
    fun onPlayerPlaying(currentWindowIndex: Int)
    fun onPlayerPaused(currentWindowIndex: Int)
    fun onPlayerBuffering(currentWindowIndex: Int)
    fun onPlayerError(errorString: String?)
    fun releaseExoPlayerCalled()
}