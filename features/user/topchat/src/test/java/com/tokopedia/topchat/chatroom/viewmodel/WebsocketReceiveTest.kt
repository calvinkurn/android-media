package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.topchat.chatroom.responses.WebsocketResponses
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.common.websocket.DefaultTopChatWebSocket
import io.mockk.coVerify
import io.mockk.every
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

    @Test
    fun should_update_typing_value_on_receive_typing_event() {
        // Given
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.typing)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.isTyping.value, true)
    }

    @Test
    fun should_do_nothing_when_msg_not_intended_for_me() {
        // Given
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.typingNotForMe)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.isTyping.value, null)
    }

    @Test
    fun should_update_typing_value_on_receive_end_typing_event() {
        // Given
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.endTyping)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.isTyping.value, false)
    }

    @Test
    fun should_update_onread_value_on_receive_read_event() {
        // Given
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.read)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.msgRead.value, Unit)
    }

    @Test
    fun should_not_update_onread_value_on_receive_read_event_in_the_middle_of_the_page() {
        // Given
        every { getChatUseCase.isInTheMiddleOfThePage() } returns true
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.read)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.msgRead.value, null)
    }


    @Test
    fun should_update_delete_msg_value_when_receive_delete_msg_event() {
        // Given
        val responseText = WebsocketResponses.deleteMsg
        val chat = generateChatPojoFromWsResponse(responseText)
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.msgDeleted.value, chat.replyTime)
    }

    @Test
    fun should_render_msg_when_receive_reply_event() {
        // Given
        val responseText = WebsocketResponses.generateReplyMsg()
        val chat = generateChatPojoFromWsResponse(responseText)
        val chatUiModel = topChatRoomWebSocketMessageMapper.map(chat)
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.unreadMsg.value, 0)
        assertEquals(
            (viewModel.newMsg.value as MessageUiModel).localId,
            (chatUiModel as MessageUiModel).localId
        )
    }
}