package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.websocket.WebSocketResponse
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//TODO: mock json response
class WebSocketViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val webSocket: TopChatWebSocket = mockk(relaxed = true)
    private val dispatchers = TopchatCoroutineContextProvider()

    private val viewModel = WebSocketViewModel(dispatchers, webSocket)

    private val itemChatObserver: Observer<Result<BaseIncomingItemWebSocketModel>> = mockk(relaxed = true)

    @Before fun setUp() {
        viewModel.itemChat.observeForever(itemChatObserver)
    }

    @Test fun `connectWebSocket should response EVENT_TOPCHAT_REPLY_MESSAGE`() {
        runBlocking {
            // Given
            val expectedValue = WebSocketResponse().apply {
                code = EVENT_TOPCHAT_REPLY_MESSAGE
                type = "test-isfa"
            }

            val channel = Channel<WebSocketResponse>()
            launch { channel.send(expectedValue) }

            coEvery { webSocket.createWebSocket() } returns channel

            // When
            viewModel.connectWebSocket()

            // Then
//            verify(exactly = 1) { wsResponseObserver.onChanged(expectedValue) }
//            assertTrue(viewModel.testWebSocketResult.value == expectedValue)

            channel.close()
        }
    }

}