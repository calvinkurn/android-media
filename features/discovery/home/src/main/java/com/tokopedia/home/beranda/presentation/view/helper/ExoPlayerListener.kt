package com.tokopedia.home.beranda.presentation.view.helper

interface ExoPlayerListener {
    fun onPlayerIdle()
    fun onPlayerPlaying()
    fun onPlayerPaused()
    fun onPlayerError(errorString: String?)
}