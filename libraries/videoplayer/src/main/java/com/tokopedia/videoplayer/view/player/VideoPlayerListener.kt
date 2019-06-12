package com.tokopedia.videoplayer.view.player

import java.io.Serializable

interface VideoPlayerListener: Serializable {
    //state changed
    //you can see any states of playback here: `com.google.android.exoplayer2.Player`
    fun onPlayerStateChanged(playbackState: Int)

    //on error
    fun onPlayerError()
}