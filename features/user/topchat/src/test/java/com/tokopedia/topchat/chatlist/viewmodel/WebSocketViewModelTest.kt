package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingTypeState
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.websocket.WebSocketResponse
import io.mockk.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WebSocketViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val webSocket: DefaultTopChatWebSocket = mockk(relaxed = true)
    private val dispatchers = CoroutineTestDispatchersProvider
    private val viewModel = WebSocketViewModel(webSocket, dispatchers)

    private val itemChatObserver: Observer<Result<BaseIncomingItemWebSocketModel>> = mockk(relaxed = true)

    @Before fun setUp() {
        viewModel.itemChat.observeForever(itemChatObserver)
    }

    @Test fun `connectWebSocket should be do nothing`() {
        runBlocking {
            // Given
            val channel = Channel<WebSocketResponse>()
            launch { channel.send(eventReplyMessage) }
            coEvery { webSocket.createWebSocket() } returns channel

            viewModel.isOnStop = true

            // When
            viewModel.connectWebSocket()

            // Then
            assertTrue(viewModel.itemChat.value == null)
        }
    }

    @Test fun `connectWebSocket should response EVENT_TOPCHAT_REPLY_MESSAGE`() {
        runBlocking {
            // Given
            val expectedValue = Success(mapToIncomingChat(eventReplyMessage))

            val channel = Channel<WebSocketResponse>()
            launch { channel.send(eventReplyMessage) }

            coEvery { webSocket.createWebSocket() } returns channel

            // When
            viewModel.connectWebSocket()

            // Then
            verify(exactly = 1) { itemChatObserver.onChanged(expectedValue) }
            assertTrue(viewModel.itemChat.value == expectedValue)

            channel.close()
        }
    }

    @Test fun `connectWebSocket should response EVENT_TOPCHAT_TYPING`() {
        runBlocking {
            // Given
            val response = mapToIncomingTypeState(eventTyping, true)
            val expectedValue = Success(response)

            val channel = Channel<WebSocketResponse>()
            launch { channel.send(eventTyping) }

            coEvery { webSocket.createWebSocket() } returns channel

            // When
            viewModel.connectWebSocket()

            // Then
            verify(exactly = 1) { itemChatObserver.onChanged(expectedValue) }
            assertTrue(viewModel.itemChat.value == expectedValue)

            channel.close()
        }
    }

    @Test fun `connectWebSocket should response EVENT_TOPCHAT_END_TYPING`() {
        runBlocking {
            // Given
            val response = mapToIncomingTypeState(eventEndTyping, false)
            val expectedValue = Success(response)

            val channel = Channel<WebSocketResponse>()
            launch { channel.send(eventEndTyping) }

            coEvery { webSocket.createWebSocket() } returns channel

            // When
            viewModel.connectWebSocket()

            // Then
            verify(exactly = 1) { itemChatObserver.onChanged(expectedValue) }
            assertTrue(viewModel.itemChat.value == expectedValue)

            channel.close()
        }
    }

    @Test fun `clearItemChatValue should clearning the value of itemChat`() {
        // When
        viewModel.clearItemChatValue()

        // Then
        assertTrue(viewModel.itemChat.value == null)
    }

    @Test fun `onStop should return isOnStop true`() {
        // When
        viewModel.onStop()

        // Then
        assertTrue(viewModel.isOnStop)
    }

    @Test fun `onStart should return isOnStop false`() {
        // When
        viewModel.onStart()

        // Then
        assertTrue(!viewModel.isOnStop)
    }

    @Test fun `onCleared should cancelling the webSocket`() {
        // Given
        val viewModelSpyk = spyk(WebSocketViewModelTest())
        every { webSocket.cancel() } just runs

        // When
        viewModelSpyk.onClearedTest()

        // Then
        verify(exactly = 1) { webSocket.cancel() }
    }

    internal inner class WebSocketViewModelTest
        : WebSocketViewModel(webSocket, dispatchers) {
        fun onClearedTest() = onCleared()
    }

    companion object {
        private val eventReplyMessage: WebSocketResponse = FileUtil.parse(
                "/ws_response_reply_text_is_opposite.json",
                WebSocketResponse::class.java
        )

        private val eventTyping: WebSocketResponse = FileUtil.parse(
                "/ws_response_typing.json",
                WebSocketResponse::class.java
        )

        private val eventEndTyping: WebSocketResponse = FileUtil.parse(
                "/ws_response_end_typing.json",
                WebSocketResponse::class.java
        )
    }

}