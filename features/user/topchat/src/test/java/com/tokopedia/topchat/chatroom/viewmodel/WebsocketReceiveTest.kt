package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.common.websocket.DefaultTopChatWebSocket
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class WebsocketReceiveTest : BaseTopChatViewModelTest() {

    @Test
    fun should_close_websocket_on_destroy_host() {
        // When
        viewModel.onDestroy()

        // Then
        verify {
            chatWebSocket.close()
            chatWebSocket.destroy()
        }
    }

    @Test
    fun should_send_mark_as_read_on_open_websocket() {
        // Given
        onConnectWebsocket {
            it.onOpen(websocket, websocketResponse)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.isWebsocketError.value, false)
        verify {
            webSocketStateHandler.retrySucceed()
        }
        verifySendMarkAsRead()
    }

    @Test
    fun should_do_nothing_on_closing_websocket() {
        // Given
        onConnectWebsocket {
            it.onClosing(websocket, DefaultTopChatWebSocket.CODE_NORMAL_CLOSURE, "")
        }

        // When
        viewModel.connectWebSocket()
    }
}