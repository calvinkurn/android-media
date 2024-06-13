package com.tokopedia.play_common.sse

import com.tokopedia.play_common.sse.model.SSEAction
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
interface PlayChannelSSE {

    fun connect(
        channelId: String,
        pageSource: String,
        gcToken: String,
        isRetry: Boolean,
    )

    fun close()

    fun listen(): Flow<SSEAction>
}
