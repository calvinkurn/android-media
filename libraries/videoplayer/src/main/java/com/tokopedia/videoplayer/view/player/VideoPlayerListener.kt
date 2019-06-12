package com.tokopedia.videoplayer.view.player

interface VideoPlayerListener {
    //state changed
    //you can see state of playback here: `com.google.android.exoplayer2.Player`
    fun onPlayerStateChanged(playbackState: Int)

    //on error
    fun onPlayerError()
}