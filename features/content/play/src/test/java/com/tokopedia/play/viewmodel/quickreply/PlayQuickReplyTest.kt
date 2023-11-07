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
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Rule
import org.junit.Test

class PlayQuickReplyTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val modelBuilder = UiModelBuilder.get()

    private val repo: PlayViewerRepository = mockk(relaxed = true)
    private val gson = Gson()

    @Test
    fun `given empty quick replies, when on init, then it should return empty quick replies`() {
        val emptyQuickReplies = emptyList<String>()
        val emptyQuickReplyInfo = modelBuilder.buildChannelData(
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
        val mockQuickReplyInfo = modelBuilder.buildChannelData(
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
            val state = it.recordState {
                createPage(repo.getChannelData("1")!!)
            }
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

        val mockQuickReplyInfo = modelBuilder.buildChannelData(
            quickReplyInfo = modelBuilder.buildQuickReply(
                quickReplyList = emptyList()
            )
        )
        every { repo.getChannelData(any()) } returns mockQuickReplyInfo

        every { mockSocket.listenAsFlow() } returns socketFlow

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo,
            playChannelWebSocket = mockSocket,
        )

        robot.use {
            val state = it.recordState {
                focusPage(mockQuickReplyInfo)
                createPage(mockQuickReplyInfo)
                socketFlow.emit(
                    WebSocketAction.NewMessage(mockQuickRepliesSocketResponse)
                )
            }
            state.quickReply.quickReplyList
                .assertEqualTo(PlayQuickReplySocketResponse.quickReplyList)
        }
    }
}
