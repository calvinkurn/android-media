package com.tokopedia.video_widget

interface ExoPlayerControl {
    fun init()
    fun play(url: String)
    fun stop()
    fun preparePlayer()
    fun onViewAttach()
    fun onViewDetach()
    fun releasePlayer()
    fun playerPause()
    fun isPlayerPlaying(): Boolean
}
