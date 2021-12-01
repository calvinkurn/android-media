package com.tokopedia.play_common.websocket

import kotlinx.coroutines.flow.Flow

/**
 * Created by jegul on 09/03/21
 */
interface PlayWebSocket {

    fun listenAsFlow(): Flow<WebSocketAction>

    fun send(message: String)

    fun connect(url: String, channelId: String, gcToken: String)

    fun close()
}