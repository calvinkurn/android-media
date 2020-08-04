package com.tokopedia.home.beranda.presentation.view.helper


interface ExoPlayerControl {
    fun init()
    fun play(url: String)
    fun preparePlayer()
    fun onViewAttach()
    fun onViewDetach()
    fun releasePlayer()
    fun playerPause()
    fun playerPlayWithDelay()
    fun isPlayerPlaying(): Boolean
    fun setExoPlayerEventsListener(pExoPlayerListenerListener: ExoPlayerListener?)
    fun onActivityResume()
    fun onActivityPause()
    fun onActivityDestroy()
}