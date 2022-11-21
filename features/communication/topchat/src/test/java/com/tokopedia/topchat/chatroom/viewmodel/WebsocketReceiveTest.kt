package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.topchat.chatroom.responses.WebsocketResponses
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.common.websocket.DefaultTopChatWebSocket
import io.mockk.*
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
    fun should_does_not_send_mark_as_read() {
        // Given
        val isFromBubble = true
        viewModel.isFromBubble = isFromBubble

        // When
        viewModel.onStop()
        viewModel.markAsRead()

        // Then
        assertEquals(viewModel.isFromBubble, true)
        assertEquals(viewModel.isOnStop, true)
        verify(exactly = 0) {
            val payload = payloadGenerator.generateMarkAsReadPayload(viewModel.roomMetaData)
            chatWebSocket.sendPayload(payload)
        }
    }

    @Test
    fun should_send_mark_as_read_when_not_from_bubble() {
        // Given
        val isFromBubble = false
        viewModel.isFromBubble = isFromBubble
        viewModel.isOnStop = false

        // When
        viewModel.markAsRead()

        // Then
        assertEquals(viewModel.isFromBubble, false)
        assertEquals(viewModel.isOnStop, false)
        verify {
            val payload = payloadGenerator.generateMarkAsReadPayload(viewModel.roomMetaData)
            chatWebSocket.sendPayload(payload)
        }
    }

    @Test
    fun should_send_mark_as_read_when_not_from_bubble_and_onstop() {
        // Given
        val isFromBubble = false
        viewModel.isFromBubble = isFromBubble

        // When
        viewModel.onStop()
        viewModel.markAsRead()

        // Then
        assertEquals(viewModel.isFromBubble, false)
        assertEquals(viewModel.isOnStop, true)
        verify {
            val payload = payloadGenerator.generateMarkAsReadPayload(viewModel.roomMetaData)
            chatWebSocket.sendPayload(payload)
        }
    }

    @Test
    fun should_send_mark_as_read_when_from_bubble_and_onresume() {
        // Given
        val isFromBubble = true
        viewModel.isFromBubble = isFromBubble

        // When
        viewModel.onResume()
        viewModel.markAsRead()

        // Then
        assertEquals(viewModel.isFromBubble, true)
        assertEquals(viewModel.isOnStop, false)
        verify {
            val payload = payloadGenerator.generateMarkAsReadPayload(viewModel.roomMetaData)
            chatWebSocket.sendPayload(payload)
        }
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

    @Test
    fun should_render_msg_and_unread_msg_is_more_than_zero_when_receive_reply_event_and_from_bubble_and_onstop() {
        // Given
        val responseText = WebsocketResponses.generateReplyMsg(
            isOpposite = true
        )
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }
        val isFromBubble = true
        viewModel.isFromBubble = isFromBubble

        // When
        viewModel.onStop()
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.unreadMsg.value, 1)
    }

    @Test
    fun should_render_msg_and_unread_msg_is_more_than_zero_when_receive_reply_event_and_not_from_bubble_and_onstop() {
        // Given
        val responseText = WebsocketResponses.generateReplyMsg(
            isOpposite = true
        )
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }
        val isFromBubble = true
        viewModel.isFromBubble = isFromBubble

        // When
        viewModel.onResume()
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.unreadMsg.value, 0)
    }

    @Test
    fun should_render_msg_and_unread_msg_is_more_than_zero_when_receive_reply_event_and_from_bubble_and_not_onstop() {
        // Given
        val responseText = WebsocketResponses.generateReplyMsg(
            isOpposite = true
        )
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }
        val isFromBubble = true
        viewModel.isFromBubble = isFromBubble

        // When
        viewModel.onStop()
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.unreadMsg.value, 1)
    }

    @Test
    fun should_render_msg_when_receive_reply_event_from_opposite() {
        // Given
        val responseText = WebsocketResponses.generateReplyMsg(
            isOpposite = true
        )
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        verifySendMarkAsRead()
    }

    @Test
    fun should_increment_unread_msg_when_receive_reply_from_opposite_in_the_middle_of_the_page() {
        // Given
        val responseText = WebsocketResponses.generateReplyMsg(
            isOpposite = true
        )
        every { getChatUseCase.isInTheMiddleOfThePage() } returns true
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
            it.onMessage(websocket, responseText)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.unreadMsg.value, 2)
    }

    @Test
    fun should_not_increment_unread_msg_when_receive_reply_from_not_opposite_in_the_middle_of_the_page() {
        // Given
        val responseText = WebsocketResponses.generateReplyMsg(
            isOpposite = false
        )
        every { getChatUseCase.isInTheMiddleOfThePage() } returns true
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.unreadMsg.value, null)
    }

    @Test
    fun should_do_nothing_from_unrecognized_ws_event() {
        // Given
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.notRecognizedEvent)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.unreadMsg.value, null)
        assertEquals(viewModel.msgDeleted.value, null)
        assertEquals(viewModel.newMsg.value, null)
    }

    @Test
    fun should_remove_srw_bubble_state_when_receive_invoice_attachment() {
        // Given
        val responseText = WebsocketResponses.generateReplyInvoice(
            isOpposite = false
        )
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(viewModel.removeSrwBubble.value, null)
    }

    @Test
    fun should_remove_srw_bubble_state_when_receive_product_attachment() {
        // Given
        val responseText = WebsocketResponses.generateReplyProduct(
            isOpposite = false
        )
        val chat = generateChatPojoFromWsResponse(responseText)
        val chatUiModel = topChatRoomWebSocketMessageMapper.map(chat)
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assertEquals(
            viewModel.removeSrwBubble.value,
            (chatUiModel as ProductAttachmentUiModel).productId
        )
    }

    @Test
    fun should_reset_unread_msg() {
        // When
        viewModel.resetUnreadMessage()

        // Then
        assertEquals(viewModel.unreadMsg.value, 0)
    }
}
