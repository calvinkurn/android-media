package com.tokopedia.play.util.channel.state

/**
 * Created by jegul on 29/09/20
 */
interface PlayViewerChannelStateListener {

    fun onChannelFreezeStateChanged(shouldFreeze: Boolean)
}