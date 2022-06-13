package com.tokopedia.play.broadcaster.util.countup

/**
 * Created By : Jonathan Darwin on November 26, 2021
 */
interface PlayCountUp {

    fun setListener(listener: PlayCountUpListener)

    fun start(duration: Long, maxDuration: Long)

    fun stop()
}