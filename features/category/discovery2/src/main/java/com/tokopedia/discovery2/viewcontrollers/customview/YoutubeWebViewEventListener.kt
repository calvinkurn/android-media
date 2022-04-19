package com.tokopedia.discovery2.viewcontrollers.customview

object YoutubeWebViewEventListener {

    interface EventVideoEnded {
        fun onVideoEnded(time: Int)
    }

    interface EventVideoPlaying {
        fun onVideoPlaying(time: Int)
    }

    interface EventVideoPaused {
        fun onVideoPaused(time: Int)
    }

    interface EventVideoBuffering {
        fun onVideoBuffering()
    }

    interface EventVideoCued {
        fun onVideoCued()
    }

    interface EventPlayerReady{
        fun onPlayerReady()
    }

}