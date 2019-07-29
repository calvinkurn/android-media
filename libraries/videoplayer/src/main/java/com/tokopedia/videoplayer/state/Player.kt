package com.tokopedia.videoplayer.state

interface Player {
    companion object {
        /**
         * The player does not have any media to play.
         */
        val STATE_IDLE = 1
        /**
         * The player is not able to immediately play from its current position. This state typically
         * occurs when more data needs to be loaded.
         */
        val STATE_BUFFERING = 2
        /**
         * The player is able to immediately play from its current position. The player will be playing if
         * [.getPlayWhenReady] is true, and paused otherwise.
         */
        val STATE_READY = 3
        /**
         * The player has finished playing the media.
         */
        val STATE_ENDED = 4
    }
}