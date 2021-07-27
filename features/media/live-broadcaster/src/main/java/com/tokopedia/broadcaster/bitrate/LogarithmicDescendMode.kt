package com.tokopedia.broadcaster.bitrate

import com.wmspanel.libstream.Streamer

class LogarithmicDescendMode : BitrateAdapter() {

    override fun start(streamer: Streamer, bitrate: Long, connectionId: Int) {
        super.start(streamer, bitrate, connectionId)
    }

    override fun check(audioLost: Long, videoLost: Long) {
        super.check(audioLost, videoLost)
    }

    override fun checkInterval(): Long {
        return super.checkInterval()
    }

    override fun checkDelay(): Long {
        return super.checkDelay()
    }

}