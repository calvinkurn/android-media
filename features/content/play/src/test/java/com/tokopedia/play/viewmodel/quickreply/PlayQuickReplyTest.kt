package com.tokopedia.play.viewmodel.quickreply

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.websocket.response.PlayQuickReplySocketResponse
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Rule
import org.junit.Test

class PlayQuickReplyTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val modelBuilder = UiModelBuilder.get()

    val repo: PlayViewerRepository = mockk(relaxed = true)
    private val gson = Gson()

    @Test
    fun `given empty quick replies, when on init, then it should return empty quick replies`() {
        val emptyQuickReplies = emptyList<String>()
        val emptyQuickReplyInfo = channelDataBuilder.buildChannelData(
            quickReplyInfo = modelBuilder.buildQuickReply(
                quickReplyList = emptyQuickReplies
            )
        )
        every { repo.getChannelData(any()) } returns emptyQuickReplyInfo

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo
        )

        robot.use {
            val state = it.recordState {}
            state.quickReply.quickReplyList
                .assertEqualTo(emptyQuickReplies)
        }
    }

    @Test
    fun `given some quick replies, when on init, then it should return those same quick replies`() {
        val mockQuickReplies = List(3) { "Halo $it" }
        val mockQuickReplyInfo = channelDataBuilder.buildChannelData(
            quickReplyInfo = modelBuilder.buildQuickReply(
                quickReplyList = mockQuickReplies
            )
        )
        every { repo.getChannelData(any()) } returns mockQuickReplyInfo

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo
        )

        robot.use {
            val state = it.recordState {}
            state.quickReply.quickReplyList
                .assertEqualTo(mockQuickReplies)
        }
    }

    @Test
    fun `when received new quick replies from socket, then it should update the quick replies`() {
        val mockQuickRepliesSocketResponse = gson.fromJson(
            PlayQuickReplySocketResponse.response,
            WebSocketResponse::class.java
        )

        val mockSocket: PlayWebSocket = mockk(relaxed = true)
        val socketFlow = MutableSharedFlow<WebSocketAction>()

        every { mockSocket.listenAsFlow() } returns socketFlow

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo,
            playChannelWebSocket = mockSocket,
        )

        robot.use {
            val state = it.recordState {
                focusPage(mockk(relaxed = true))
                socketFlow.emit(
                    WebSocketAction.NewMessage(mockQuickRepliesSocketResponse)
                )
            }
            state.quickReply.quickReplyList
                .assertEqualTo(PlayQuickReplySocketResponse.quickReplyList)
        }
    }
}