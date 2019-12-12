package com.tokopedia.play_common.listener

interface EventListener {

    fun onFirstFrameRendered()

    fun onBuffering() // ExoPlayer state: 2

    fun onPlaying() // ExoPlayer state: 3, play flag: true

    fun onPaused() // ExoPlayer state: 3, play flag: false

    fun onCompleted() // ExoPlayer state: 4
}