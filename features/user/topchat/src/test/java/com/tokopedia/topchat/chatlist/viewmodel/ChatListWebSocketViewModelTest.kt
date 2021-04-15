package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingTypeState
import com.tokopedia.topchat.chatlist.domain.websocket.PendingMessageHandler
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketResponse
import io.mockk.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatListWebSocketViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val webSocket: TopChatWebSocket = mockk(relaxed = true)
    private val dispatchers = CoroutineTestDispatchersProvider
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val pendingMessageHandler: PendingMessageHandler = mockk(relaxed = true)

    private val viewModel = ChatListWebSocketViewModel(
            webSocket,
            userSession,
            dispatchers,
            pendingMessageHandler
    )

    private val itemChatObserver: Observer<Result<BaseIncomingItemWebSocketModel>> = mockk(relaxed = true)

    @Before fun setUp() {
        viewModel.itemChat.observeForever(itemChatObserver)
    }

    @Test fun `onLifeCycleStart should return isOnStop false and activeRoom is empty`() {
        // When
        viewModel.onLifeCycleStart()

        // Then
        assertTrue(!viewModel.isOnStop)
        assertTrue(viewModel.activeRoom.isEmpty())
    }

    @Test fun `onLifeCycleStop should return isOnStop true`() {
        // When
        viewModel.onLifeCycleStop()

        // Then
        assertTrue(viewModel.isOnStop)
    }

    @Test fun `connectWebSocket should response EVENT_TOPCHAT_REPLY_MESSAGE`() {
        runBlocking {
            // Given
            val data = mapToIncomingChat(eventReplyMessage)
            val expectedValue = Success(data)

            val channel = Channel<WebSocketResponse>()
            launch { channel.send(eventReplyMessage) }
            coEvery { webSocket.createWebSocket() } returns channel

            viewModel.isOnStop = false
            every { pendingMessageHandler.hasPendingMessage() } returns false

            // When
            viewModel.connectWebSocket()

            // Then
            verify(exactly = 1) { itemChatObserver.onChanged(expectedValue) }
            assertTrue(viewModel.itemChat.value == expectedValue)

            channel.close()
        }
    }

    @Test fun `connectWebSocket should queue the incoming message`() {
        runBlocking {
            // Given
            val viewModelSpyk = spyk(viewModel, recordPrivateCalls = true)

            val data = mapToIncomingChat(eventReplyMessage)

            val channel = Channel<WebSocketResponse>()
            launch { channel.send(eventReplyMessage) }
            coEvery { webSocket.createWebSocket() } returns channel

            every { pendingMessageHandler.hasPendingMessage() } returns true

            // When
            viewModelSpyk.connectWebSocket()

            // Then
            verify(exactly = 1) { viewModelSpyk["queueIncomingMessage"](data) }

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

    @Test fun `onRoleChanged should able to change the role of user`() {
        // Given
        val role = RoleType.SELLER

        // When
        viewModel.onRoleChanged(role)

        // Then
        assertTrue(viewModel.role == role)
    }

    @Test fun `clearPendingMessages should clear the pending message`() {
        // Given
        every { pendingMessageHandler.pendingMessages.clear() } just runs

        // When
        viewModel.clearPendingMessages()

        // Then
        verify(exactly = 1) { pendingMessageHandler.pendingMessages.clear() }
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