package com.tokopedia.videoplayer.state

class PlayerType {
    companion object {
        /**
         * default, VOD/normal video player purpose
         * it will enabled for seekTo
         */
        val DEFAULT = 0

        /**
         * live stream
         * preventing seekTo
         */
        val LIVE_STREAM = 1
    }
}