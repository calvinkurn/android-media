package com.tokopedia.topchat.chatlist.viewmodel

import androidx.lifecycle.Lifecycle
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModelTest.Companion.eventReplyMessage
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModelTest.Companion.eventReplyMessage2
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModelTest.Companion.eventReplyMessageString
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModelTest.Companion.eventReplyMessageString2
import io.mockk.every
import io.mockk.slot
import okhttp3.WebSocketListener
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * test ChatListWebSocketViewModel.queueIncomingMessage()
 */
class ChatListWebSocketPendingMessageTest : ChatListWebSocketViewModelTest() {

    @Test
    fun should_queue_pending_message_when_on_stop() {
        // Given
        val mapResponse = mapToIncomingChat(eventReplyMessage)
        val response = eventReplyMessageString
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        viewModel.connectWebSocket()

        // Then
        assert(viewModel.pendingMessages[mapResponse.msgId] != null)
        assertThat(viewModel.pendingMessages.size, `is`(1))
    }

    @Test
    fun should_queue_pending_message_when_pending_message_is_not_empty() {
        // Given
        val mapResponse = mapToIncomingChat(eventReplyMessage)
        val mapResponseSecond = mapToIncomingChat(eventReplyMessage2)
        val response = eventReplyMessageString
        val responseSecond = eventReplyMessageString2
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        viewModel.connectWebSocket()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        webSocketListener.captured.onMessage(webSocket, responseSecond)

        // Then
        assert(viewModel.pendingMessages[mapResponse.msgId] != null)
        assert(viewModel.pendingMessages[mapResponseSecond.msgId] != null)
        assertThat(viewModel.pendingMessages.size, `is`(2))
    }

}