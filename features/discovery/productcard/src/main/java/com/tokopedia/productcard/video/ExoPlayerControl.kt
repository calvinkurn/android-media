package com.tokopedia.productcard.video

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
    fun setExoPlayerEventsListener(exoPlayerListener: ExoPlayerListener?)
}