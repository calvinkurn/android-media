package com.tokopedia.play.broadcaster.viewmodel.logger

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.statistic.PlayBroadcasterMetric
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import org.junit.Rule
import org.junit.Test

/**
 * Created by meyta.taliti on 26/09/22.
 */
class PlayBroadcastLoggerTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    @Test
    fun `when broadcaster start throw error, then it should trigger logger error`() {
        val mockLogger: PlayLogger = mockk(relaxed = true)
        val mockHydraConfigStore: HydraConfigStore = mockk(relaxed = true)

        val expectedException = Throwable()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            logger = mockLogger,
            hydraConfigStore = mockHydraConfigStore,
        )

        val mock = spyk(robot.getViewModel(), recordPrivateCalls = true)

        every { mockHydraConfigStore.getChannelId() } returns "1234"
        coEvery { mock invoke "getChannelById" withArguments listOf("1234") } throws expectedException

        robot.use {
            mock.submitAction(
                PlayBroadcastAction.BroadcastStateChanged(
                    PlayBroadcasterState.Started
                )
            )

            verify { mockLogger.logBroadcastError(expectedException) }
        }
    }

    @Test
    fun `when broadcaster pause throw error, then it should trigger logger error`() {
        val mockLogger: PlayLogger = mockk(relaxed = true)

        val expectedException = Throwable()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            logger = mockLogger,
        )

        val mock = spyk(robot.getViewModel(), recordPrivateCalls = true)

        coEvery { mock invoke "updateChannelStatus" withArguments listOf(PlayChannelStatusType.Pause) } throws expectedException

        robot.use {
            mock.submitAction(
                PlayBroadcastAction.BroadcastStateChanged(
                    PlayBroadcasterState.Paused
                )
            )

            verify { mockLogger.logBroadcastError(expectedException) }
        }
    }

    @Test
    fun `when broadcaster stop throw error, then it should trigger logger error`() {
        val mockLogger: PlayLogger = mockk(relaxed = true)
        val mockPlayBroadcastWebSocket: PlayWebSocket = mockk(relaxed = true)

        val expectedException = Throwable()

        every { mockPlayBroadcastWebSocket.close() } throws expectedException

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            logger = mockLogger,
            playBroadcastWebSocket = mockPlayBroadcastWebSocket,
        )

        robot.use {
            it.getViewModel().submitAction(
                PlayBroadcastAction.BroadcastStateChanged(
                    PlayBroadcasterState.Stopped
                )
            )

            verify { mockLogger.logBroadcastError(expectedException) }
        }
    }

    @Test
    fun `when send broadcaster metrics, then it should call logger sendBroadcasterLog`() {
        val mockMetric = BroadcasterMetric.Empty
        val mockLogger: PlayLogger = mockk(relaxed = true)

        val mappedMetric = PlayBroadcasterMetric(
            authorId = "",
            channelId = "",
            videoBitrate = mockMetric.videoBitrate,
            audioBitrate = mockMetric.audioBitrate,
            resolution = "${mockMetric.resolutionWidth}x${mockMetric.resolutionHeight}",
            traffic = mockMetric.traffic,
            bandwidth = mockMetric.bandwidth,
            fps = mockMetric.fps,
            packetLossIncreased = mockMetric.packetLossIncreased,
            videoBufferTimestamp = mockMetric.videoBufferTimestamp,
            audioBufferTimestamp = mockMetric.audioBufferTimestamp,
        )

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            logger = mockLogger,
        )

        robot.use {
            it.getViewModel().sendBroadcasterLog(mockMetric)

            verify { mockLogger.sendBroadcasterLog(mappedMetric) }
        }
    }

    @Test
    fun `when send all logs, then it should call logger sendAll`() {
        val mockLogger: PlayLogger = mockk(relaxed = true)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            logger = mockLogger,
        )

        robot.use {
            it.getViewModel().sendLogs()

            verify { mockLogger.sendAll("") }
        }
    }
}
