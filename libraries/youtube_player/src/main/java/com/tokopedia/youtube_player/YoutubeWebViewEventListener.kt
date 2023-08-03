package com.tokopedia.youtube_player

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

    interface EventPlayerReady {
        fun onPlayerReady()
    }

    interface EventError {
        fun onError(errorCode: Int)
    }
}
