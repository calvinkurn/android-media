package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.common.websocket.DefaultTopChatWebSocket
import io.mockk.coVerify
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalStateException

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

    @Test
    fun should_retry_connect_websocket_when_ws_closed_abnormally() {
        // Given
        onConnectWebsocket {
            it.onClosed(websocket, 500, "")
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.isWebsocketError.value, true)
        coVerify {
            chatWebSocket.close()
            webSocketStateHandler.scheduleForRetry(any())
        }
    }

    @Test
    fun should_not_retry_connect_websocket_when_ws_closed_normally() {
        // Given
        onConnectWebsocket {
            it.onClosed(websocket, DefaultTopChatWebSocket.CODE_NORMAL_CLOSURE, "")
        }

        // When
        viewModel.connectWebSocket()

        // Then
        coVerify(exactly = 0) {
            chatWebSocket.close()
            webSocketStateHandler.scheduleForRetry(any())
        }
    }

    @Test
    fun should_retry_connect_websocket_when_ws_failed() {
        // Given
        onConnectWebsocket {
            it.onFailure(websocket, IllegalStateException(), websocketResponse)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.isWebsocketError.value, true)
        coVerify {
            chatWebSocket.close()
            webSocketStateHandler.scheduleForRetry(any())
        }
    }
}