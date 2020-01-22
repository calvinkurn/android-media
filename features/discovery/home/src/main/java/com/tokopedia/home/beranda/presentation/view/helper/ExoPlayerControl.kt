package com.tokopedia.home.beranda.presentation.view.helper


interface ExoPlayerControl {
    fun init()
    fun createPlayer()
    fun onViewAttach()
    fun onViewDetach()
    fun releasePlayer()
    fun playerPause()
    fun playerPlay()
    fun isPlayerNull(): Boolean
    fun isPlayerPlaying(): Boolean
    fun seekToDefaultPosition()
    fun setExoPlayerEventsListener(pExoPlayerListenerListener: ExoPlayerListener?)
    fun onActivityResume()
    fun onActivityPause()
    fun onActivityStop()
}