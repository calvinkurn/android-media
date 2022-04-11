package com.tokopedia.play.broadcaster.util.countup

/**
 * Created By : Jonathan Darwin on November 26, 2021
 */
interface PlayCountUpListener {

    fun onTick(duration: Long)

    fun onFinish()
}