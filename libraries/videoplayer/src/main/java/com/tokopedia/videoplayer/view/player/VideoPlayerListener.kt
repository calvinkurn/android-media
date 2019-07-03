package com.tokopedia.videoplayer.view.player

import com.tokopedia.videoplayer.utils.PlayerException
import com.tokopedia.videoplayer.utils.RepeatMode
import java.io.Serializable

interface VideoPlayerListener: Serializable {
    /**
     * Handle state changed on ExoPlayer
     * you can see any states of playback here: `com.google.android.exoplayer2.Player`
     */
    fun onPlayerStateChanged(playbackState: Int)

    /**
     * Catch a error on player level
     * @param: PlayerException
     */
    fun onPlayerError(error: PlayerException)
}