package com.tokopedia.videoplayer.view.player

import java.io.Serializable

interface VideoPlayerListener: Serializable {
    /**
     * Handle state changed on ExoPlayer
     * you can see any states of playback here: `com.google.android.exoplayer2.Player`
     */
    fun onPlayerStateChanged(playbackState: Int)

    /**
     * Catch a error on player level
     */
    fun onPlayerError()
}