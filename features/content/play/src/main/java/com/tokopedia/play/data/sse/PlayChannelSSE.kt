package com.tokopedia.play.data.sse

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
interface PlayChannelSSE {

    fun connect(channelId: String, pageSource: String, gcToken: String)

    fun close()
}