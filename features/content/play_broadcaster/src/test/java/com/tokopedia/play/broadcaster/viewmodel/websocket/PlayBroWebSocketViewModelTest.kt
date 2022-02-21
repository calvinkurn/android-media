package com.tokopedia.play.broadcaster.viewmodel.websocket

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.fake.FakePlayWebSocket
import com.tokopedia.play.broadcaster.model.websocket.WebSocketUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 21, 2022
 */
class PlayBroWebSocketViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val fakePlayWebSocket= FakePlayWebSocket(testDispatcher)

    private val webSocketUiModelBuilder = WebSocketUiModelBuilder()

    @Test
    fun `when user received new total view event, then it should emit new total view data`() {
        val mockTotalViewString = webSocketUiModelBuilder.buildTotalViewString()
        val mockTotalView = webSocketUiModelBuilder.buildTotalViewModel()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockTotalViewString)

            val result = robot.getViewModel().observableTotalView.getOrAwaitValue()

            result.assertEqualTo(mockTotalView)
        }
    }

    @Test
    fun `when user received new total like event, then it should emit new total like data`() {
        val mockTotalLikeString = webSocketUiModelBuilder.buildTotalLikeString()
        val mockTotalLike = webSocketUiModelBuilder.buildTotalLikeModel()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            playBroadcastWebSocket = fakePlayWebSocket,
        )

        robot.use {
            robot.executeViewModelPrivateFunction("startWebSocket")
            fakePlayWebSocket.fakeEmitMessage(mockTotalLikeString)

            val result = robot.getViewModel().observableTotalLike.getOrAwaitValue()

            result.assertEqualTo(mockTotalLike)
        }
    }
}