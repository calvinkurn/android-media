package com.tokopedia.play.util.video.state

/**
 * Created by jegul on 28/08/20
 */
interface PlayViewerVideoStateProcessor {

    fun addStateListener(listener: PlayViewerVideoStateListener)

    fun removeStateListener(listener: PlayViewerVideoStateListener)
}