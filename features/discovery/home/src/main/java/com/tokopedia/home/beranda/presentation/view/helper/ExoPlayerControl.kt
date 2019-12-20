package com.tokopedia.home.beranda.presentation.view.helper


interface ExoPlayerControl {
    fun createPlayer(isToPrepare: Boolean)
    fun preparePlayer()
    fun releasePlayer()
    fun playerPause()
    fun playerPlay()
    fun seekTo(windowIndex: Int, positionMs: Long)
    fun seekToDefaultPosition()
    fun setExoPlayerEventsListener(pExoPlayerListenerListener: ExoPlayerListener?)
    fun setExoThumbListener(exoThumbListener: ExoThumbListener?)
    fun onActivityStart()
    fun onActivityResume()
    fun onActivityPause()
    fun onActivityStop()
}