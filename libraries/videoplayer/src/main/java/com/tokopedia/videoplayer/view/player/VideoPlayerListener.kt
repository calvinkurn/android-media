package com.tokopedia.videoplayer.view.player

abstract class VideoPlayerListener {
    //state changed
    //you can see state of playback here: com.google.android.exoplayer2.Player
    abstract fun onPlayerStateChanged(playbackState: Int)

    //on error
    abstract fun onPlayerError()

    //thumbnail
    abstract fun showThumbnail(imageFile: String)
}