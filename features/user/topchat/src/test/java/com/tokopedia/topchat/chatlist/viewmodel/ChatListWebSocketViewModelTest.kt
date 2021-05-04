package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultTopChatWebSocket
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultWebSocketParser
import com.tokopedia.topchat.chatlist.domain.websocket.PendingMessageHandler
import com.tokopedia.topchat.chatlist.domain.websocket.WebSocketStateHandler
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModelTest.Companion.eventReplyMessage
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModelTest.Companion.eventReplyMessageString
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertThat
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
    private val pendingMessageHandler: PendingMessageHandler = PendingMessageHandler(userSession)
    private val webSocket: WebSocket = mockk(relaxed = true)
    private val webSocketStateHandler: WebSocketStateHandler = mockk(relaxed = true)
    private val lifecycleRegistry = LifecycleRegistry(mockk(relaxed = true))

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
        lifecycleRegistry.addObserver(viewModel)
    }

    @After
    fun tearDown() {
        lifecycleRegistry.removeObserver(viewModel)
    }

    @Test
    fun onLifeCycleStart_should_return_isOnStop_false_and_activeRoom_is_empty() {
        // When
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)

        // Then
        assertThat(viewModel.isOnStop, `is`(false))
        assertThat(viewModel.activeRoom, `is`(""))
    }

    @Test
    fun onLifeCycleStop_should_return_isOnStop_true() {
        // When
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)

        // Then
        assertThat(viewModel.isOnStop, `is`(true))
    }

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
    fun `onRoleChanged should able to change the role of user`() {
        // Given
        val role = RoleType.SELLER

        // When
        viewModel.onRoleChanged(role)

        // Then
        assertTrue(viewModel.role == role)
    }

    @Test
    fun should_have_no_pending_message_when_cleared() {
        // Given
        val response = eventReplyMessageString
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        viewModel.connectWebSocket()
        viewModel.clearPendingMessages()

        // Then
        assertThat(viewModel.pendingMessages.size, `is`(0))
    }

    @Test
    fun should_immediately_update_latest_itemChat() {
        // Given
        val expectedValue = mapToIncomingChat(eventReplyMessage)
        val response = eventReplyMessageString
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        viewModel.connectWebSocket()

        // Then
        val data = (viewModel.itemChat.value as? Success)?.data
        assertThat(viewModel.itemChat.value, `is`(instanceOf(Success::class.java)))
        assertThat(data?.messageId, `is`(expectedValue.messageId))
    }

    @Test
    fun should_have_reduced_pending_message_when_msgId_deleted() {
        // Given
        val expectedValue = mapToIncomingChat(eventReplyMessage)
        val response = eventReplyMessageString
        val webSocketListener = slot<WebSocketListener>()
        every { topchatWebSocket.connectWebSocket(capture(webSocketListener)) } answers {
            webSocketListener.captured.onMessage(webSocket, response)
        }

        // When
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        viewModel.connectWebSocket()
        viewModel.deletePendingMsgWithId(expectedValue.msgId)

        // Then
        assertThat(viewModel.pendingMessages.size, `is`(0))
    }

    @Test
    fun should_return_the_latest_role_assigned() {
        // When
        viewModel.role = RoleType.SELLER

        // Then
        assertThat(viewModel.role, `is`(RoleType.SELLER))
    }

    @Test
    fun should_return_the_latest_activeRoom_assigned() {
        // Given
        val msgId = "111"

        // When
        viewModel.activeRoom = msgId

        // Then
        assertThat(viewModel.activeRoom, `is`(msgId))
    }

}