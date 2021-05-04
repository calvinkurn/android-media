package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingTypeState
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultTopChatWebSocket
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultWebSocketParser
import com.tokopedia.topchat.chatlist.domain.websocket.WebSocketStateHandler
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.websocket.WebSocketResponse
import io.mockk.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WebSocketViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val topchatWebSocket: DefaultTopChatWebSocket = mockk(relaxed = true)
    private val webSocket: WebSocket = mockk(relaxed = true)
    private val dispatchers = CoroutineTestDispatchersProvider
    private val webSocketStateHandler: WebSocketStateHandler = mockk(relaxed = true)
    private val viewModel = WebSocketViewModel(
            topchatWebSocket, DefaultWebSocketParser(), webSocketStateHandler, dispatchers
    )

    private val itemChatObserver: Observer<Result<BaseIncomingItemWebSocketModel>> = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel.itemChat.observeForever(itemChatObserver)
    }

    @Test
    fun `connectWebSocket should be do nothing`() {
        // Given
        every { topchatWebSocket.connectWebSocket(any()) } just Runs

        // When
        viewModel.connectWebSocket()

        // Then
        assertTrue(viewModel.itemChat.value == null)
    }

    @Test
    fun `connectWebSocket should response EVENT_TOPCHAT_REPLY_MESSAGE`() {
        // Given
        val expectedValue = Success(mapToIncomingChat(eventReplyMessage))
        val response = eventReplyMessageString
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        verify(exactly = 1) { itemChatObserver.onChanged(expectedValue) }
    }

    @Test
    fun `connectWebSocket should response EVENT_TOPCHAT_TYPING`() {
        // Given
        val expectedValue = Success(mapToIncomingTypeState(eventTyping, true))
        val response = eventTypingString
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        verify(exactly = 1) { itemChatObserver.onChanged(expectedValue) }
    }

    @Test
    fun `connectWebSocket should response EVENT_TOPCHAT_END_TYPING`() {
        val expectedValue = Success(mapToIncomingTypeState(eventEndTyping, false))
        val response = eventEndTypingString
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        verify(exactly = 1) { itemChatObserver.onChanged(expectedValue) }
    }

    @Test
    fun should_not_do_anything_when_receive_unknown_event() {
        val response = eventUnknownEvent
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        assert(viewModel.itemChat.value == null)
    }

    @Test
    fun `clearItemChatValue should clearning the value of itemChat`() {
        // When
        viewModel.clearItemChatValue()

        // Then
        assertTrue(viewModel.itemChat.value == null)
    }

    @Test
    fun `onStop should return isOnStop true`() {
        // When
        viewModel.onStop()

        // Then
        assertTrue(viewModel.isOnStop)
    }

    @Test
    fun `onStart should return isOnStop false`() {
        // When
        viewModel.onStart()

        // Then
        assertTrue(!viewModel.isOnStop)
    }

    @Test
    fun should_not_handle_any_event_when_onStop_is_true() {
        // Given
        val response = eventReplyMessageString
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        viewModel.onStop()
        viewModel.connectWebSocket()

        // Then
        assert(viewModel.itemChat.value == null)
    }

    @Test
    fun should_call_retry_succeed_when_open_connection_to_ws() {
        // Given
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onOpen(webSocket, mockk(relaxed = true))
        }

        // When
        viewModel.connectWebSocket()

        // Then
        verify(exactly = 1) { webSocketStateHandler.retrySucceed() }
    }

    companion object {
        val eventReplyMessageString = com.tokopedia.topchat.FileUtil.readFileContent(
                "/ws_response_reply_text_is_opposite.json"
        )
        val eventReplyMessage: WebSocketResponse = com.tokopedia.topchat.FileUtil.parseContent(
                eventReplyMessageString,
                WebSocketResponse::class.java
        )

        val eventTypingString = com.tokopedia.topchat.FileUtil.readFileContent(
                "/ws_response_typing.json"
        )
        val eventTyping: WebSocketResponse = com.tokopedia.topchat.FileUtil.parseContent(
                eventTypingString,
                WebSocketResponse::class.java
        )

        val eventEndTypingString = com.tokopedia.topchat.FileUtil.readFileContent(
                "/ws_response_end_typing.json"
        )
        val eventEndTyping: WebSocketResponse = com.tokopedia.topchat.FileUtil.parseContent(
                eventEndTypingString,
                WebSocketResponse::class.java
        )

        val eventUnknownEvent = com.tokopedia.topchat.FileUtil.readFileContent(
                "/ws_response_unknown_event.json"
        )
    }

}