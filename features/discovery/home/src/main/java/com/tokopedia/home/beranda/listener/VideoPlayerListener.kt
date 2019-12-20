package com.tokopedia.home.beranda.listener

import com.google.android.exoplayer2.ExoPlayer

interface VideoPlayerListener {
    fun getPlayer(): ExoPlayer
    fun playVideo(uri: String)
    fun addListener(listener: ExoListener)
}

interface ExoListener{
    fun onBuffering()
    fun stopVideo()
    fun playVideo()
    fun resetVideo()
}