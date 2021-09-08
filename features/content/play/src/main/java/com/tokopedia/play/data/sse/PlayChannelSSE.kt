package com.tokopedia.play.data.sse

import com.tokopedia.play_common.sse.SSEAction
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
interface PlayChannelSSE {

    fun connect(channelId: String, pageSource: String, gcToken: String)

    fun close()

    fun listen(): Flow<SSEAction>
}