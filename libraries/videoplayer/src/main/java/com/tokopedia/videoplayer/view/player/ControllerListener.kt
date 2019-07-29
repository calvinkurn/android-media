package com.tokopedia.videoplayer.view.player

interface ControllerListener {
    /**
     * on resume
     */
    fun resume()

    /**
     * on pause video in current state position
     */
    fun pause()

    /**
     * on stop video
     */
    fun stop()
}