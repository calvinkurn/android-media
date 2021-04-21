package com.tokopedia.play.data.websocket.revamp

import kotlinx.coroutines.flow.Flow

/**
 * Created by jegul on 09/03/21
 */
interface PlayWebSocket {

    fun listenAsFlow(): Flow<WebSocketAction>

    fun send(message: String)

    fun connect(url: String)

    fun close()
}