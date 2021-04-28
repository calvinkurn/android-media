package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultTopChatWebSocket
import com.tokopedia.topchat.chatlist.domain.websocket.PendingMessageHandler
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModelTest.Companion.eventReplyMessage
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModelTest.Companion.eventReplyMessageString
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultWebSocketParser
import com.tokopedia.topchat.chatlist.domain.websocket.WebSocketStateHandler
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatListWebSocketViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val topchatWebSocket: DefaultTopChatWebSocket = mockk(relaxed = true)
    private val dispatchers = CoroutineTestDispatchersProvider
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val pendingMessageHandler: PendingMessageHandler = mockk(relaxed = true)
    private val webSocket: WebSocket = mockk(relaxed = true)
    private val webSocketStateHandler: WebSocketStateHandler = mockk(relaxed = true)

    private val viewModel = ChatListWebSocketViewModel(
            topchatWebSocket,
            DefaultWebSocketParser(),
            webSocketStateHandler,
            userSession,
            dispatchers,
            pendingMessageHandler
    )

    private val itemChatObserver: Observer<Result<BaseIncomingItemWebSocketModel>> = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel.itemChat.observeForever(itemChatObserver)
    }

    @Test
    fun `onLifeCycleStart should return isOnStop false and activeRoom is empty`() {
        // When
        viewModel.onLifeCycleStart()

        // Then
        assertTrue(!viewModel.isOnStop)
        assertTrue(viewModel.activeRoom.isEmpty())
    }

    @Test
    fun `onLifeCycleStop should return isOnStop true`() {
        // When
        viewModel.onLifeCycleStop()

        // Then
        assertTrue(viewModel.isOnStop)
    }

    @Test
    fun `connectWebSocket should queue the incoming message`() {
        // Given
        val mapResponse = mapToIncomingChat(eventReplyMessage)
        val response = eventReplyMessageString
        val webSocketListener = slot<WebSocketListener>()
        every { pendingMessageHandler.hasPendingMessage() } returns true
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        viewModel.connectWebSocket()

        // Then
        verify(exactly = 1) {
            pendingMessageHandler.addQueue(viewModel.role, mapResponse, false)
        }
    }

    @Test
    fun `onRoleChanged should able to change the role of user`() {
        // Given
        val role = RoleType.SELLER

        // When
        viewModel.onRoleChanged(role)

        // Then
        assertTrue(viewModel.role == role)
    }

    @Test
    fun `clearPendingMessages should clear the pending message`() {
        // Given
        every { pendingMessageHandler.pendingMessages.clear() } just runs

        // When
        viewModel.clearPendingMessages()

        // Then
        verify(exactly = 1) { pendingMessageHandler.pendingMessages.clear() }
    }

}