package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
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
        webSocketViewModel.onDestroy {
            getDummyLifeCycle()
        }

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
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.isWebsocketError.value, false)
        verify {
            webSocketStateHandler.retrySucceed()
        }
        verifySendMarkAsRead()
    }

    @Test
    fun should_does_not_send_mark_as_read() {
        // Given
        val isFromBubble = true
        webSocketViewModel.isFromBubble = isFromBubble

        // When
        webSocketViewModel.onStop {
            getDummyLifeCycle()
        }
        webSocketViewModel.markAsRead()

        // Then
        assertEquals(webSocketViewModel.isFromBubble, true)
        assertEquals(webSocketViewModel.isOnStop, true)
        verify(exactly = 0) {
            val payload = payloadGenerator.generateMarkAsReadPayload(
                viewModel.roomMetaData.value ?: RoomMetaData()
            )
            chatWebSocket.sendPayload(payload)
        }
    }

    @Test
    fun should_send_mark_as_read_when_not_from_bubble() {
        // Given
        val isFromBubble = false
        webSocketViewModel.isFromBubble = isFromBubble
        webSocketViewModel.isOnStop = false

        // When
        webSocketViewModel.markAsRead()

        // Then
        assertEquals(webSocketViewModel.isFromBubble, false)
        assertEquals(webSocketViewModel.isOnStop, false)
        verify {
            chatWebSocket.sendPayload(any())
        }
    }

    @Test
    fun should_send_mark_as_read_when_not_from_bubble_and_onstop() {
        // Given
        val isFromBubble = false
        webSocketViewModel.isFromBubble = isFromBubble

        // When
        webSocketViewModel.onStop {
            getDummyLifeCycle()
        }
        webSocketViewModel.markAsRead()

        // Then
        assertEquals(webSocketViewModel.isFromBubble, false)
        assertEquals(webSocketViewModel.isOnStop, true)
        verify {
            chatWebSocket.sendPayload(any())
        }
    }

    @Test
    fun should_send_mark_as_read_when_from_bubble_and_onresume() {
        // Given
        val isFromBubble = true
        webSocketViewModel.isFromBubble = isFromBubble

        // When
        webSocketViewModel.onResume {
            getDummyLifeCycle()
        }
        webSocketViewModel.markAsRead()

        // Then
        assertEquals(webSocketViewModel.isFromBubble, true)
        assertEquals(webSocketViewModel.isOnStop, false)
        verify {
            chatWebSocket.sendPayload(any())
        }
    }

    @Test
    fun should_do_nothing_on_closing_websocket() {
        // Given
        onConnectWebsocket {
            it.onClosing(websocket, DefaultTopChatWebSocket.CODE_NORMAL_CLOSURE, "")
        }

        // When
        webSocketViewModel.connectWebSocket()
    }

    @Test
    fun should_retry_connect_websocket_when_ws_closed_abnormally() {
        // Given
        onConnectWebsocket {
            it.onClosed(websocket, 500, "")
        }

        // When
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.isWebsocketError.value, true)
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
        webSocketViewModel.connectWebSocket()

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
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.isWebsocketError.value, true)
        coVerify {
            chatWebSocket.close()
            webSocketStateHandler.scheduleForRetry(any())
        }
    }

    @Test
    fun should_do_nothing_when_reconnect_websocket_but_failed() {
        // Given
        onConnectWebsocket {
            it.onFailure(websocket, IllegalStateException(), websocketResponse)
        }
        coEvery {
            webSocketStateHandler.scheduleForRetry(any())
        } throws IllegalStateException()

        // When
        webSocketViewModel.connectWebSocket()

        // Then
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
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.isTyping.value, true)
    }

    @Test
    fun should_do_nothing_when_msg_not_intended_for_me() {
        // Given
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.typingNotForMe)
        }

        // When
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.isTyping.value, null)
    }

    @Test
    fun should_update_typing_value_on_receive_end_typing_event() {
        // Given
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.endTyping)
        }

        // When
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.isTyping.value, false)
    }

    @Test
    fun should_update_onread_value_on_receive_read_event() {
        // Given
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.read)
        }

        // When
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.msgRead.value, Unit)
    }

    @Test
    fun should_not_update_onread_value_on_receive_read_event_in_the_middle_of_the_page() {
        // Given
        webSocketViewModel.isInTheMiddleOfThePage = true
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.read)
        }

        // When
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.msgRead.value, null)
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
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.msgDeleted.value, chat.replyTime)
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
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.unreadMsg.value, 0)
        assertEquals(
            (webSocketViewModel.newMsg.value as MessageUiModel).localId,
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
        webSocketViewModel.isFromBubble = isFromBubble

        // When
        webSocketViewModel.onStop {
            getDummyLifeCycle()
        }
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.unreadMsg.value, 1)
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
        webSocketViewModel.isFromBubble = isFromBubble

        // When
        webSocketViewModel.onResume {
            getDummyLifeCycle()
        }
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.unreadMsg.value, 0)
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
        webSocketViewModel.isFromBubble = isFromBubble

        // When
        webSocketViewModel.onStop {
            getDummyLifeCycle()
        }
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.unreadMsg.value, 1)
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
        webSocketViewModel.connectWebSocket()

        // Then
        verifySendMarkAsRead()
    }

    @Test
    fun should_increment_unread_msg_when_receive_reply_from_opposite_in_the_middle_of_the_page() {
        // Given
        val responseText = WebsocketResponses.generateReplyMsg(
            isOpposite = true
        )
        webSocketViewModel.isInTheMiddleOfThePage = true
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
            it.onMessage(websocket, responseText)
        }

        // When
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.unreadMsg.value, 2)
    }

    @Test
    fun should_not_increment_unread_msg_when_receive_reply_from_not_opposite_in_the_middle_of_the_page() {
        // Given
        val responseText = WebsocketResponses.generateReplyMsg(
            isOpposite = false
        )
        webSocketViewModel.isInTheMiddleOfThePage = true
        onConnectWebsocket {
            it.onMessage(websocket, responseText)
        }

        // When
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.unreadMsg.value, null)
    }

    @Test
    fun should_do_nothing_from_unrecognized_ws_event() {
        // Given
        onConnectWebsocket {
            it.onMessage(websocket, WebsocketResponses.notRecognizedEvent)
        }

        // When
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.unreadMsg.value, null)
        assertEquals(webSocketViewModel.msgDeleted.value, null)
        assertEquals(webSocketViewModel.newMsg.value, null)
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
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(webSocketViewModel.removeSrwBubble.value, null)
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
        webSocketViewModel.connectWebSocket()

        // Then
        assertEquals(
            webSocketViewModel.removeSrwBubble.value,
            (chatUiModel as ProductAttachmentUiModel).productId
        )
    }

    @Test
    fun should_reset_unread_msg() {
        // When
        webSocketViewModel.resetUnreadMessage()

        // Then
        assertEquals(webSocketViewModel.unreadMsg.value, 0)
    }

    @Test
    fun when_reset_all_live_data_should_give_null() {
        // Given
        webSocketViewModel.resetUnreadMessage()

        // When
        webSocketViewModel.resetMessageLiveData()

        // Then
        assertEquals(null, webSocketViewModel.unreadMsg.value)
    }
}
