package com.tokopedia.play.broadcaster.viewmodel.summary.report

import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastSummaryViewModelRobot
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.util.*
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.fail

/**
 * Created by jegul on 25/09/20
 */
class PlayBroadcastSummaryViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val playBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer())

    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetLiveStatisticsUseCase: GetLiveStatisticsUseCase = mockk(relaxed = true)

    private val modelBuilder = UiModelBuilder()
    private val mockException = Exception("Network Error")
    private val mockLiveStats by lazy { modelBuilder.buildLiveStats() }
    private val mockPublishedAt = "2022-03-10T18:44:00"
    private val mockPublishedAtFormatted = "10 Maret 2022"
    private val mockChannel = GetChannelResponse.Channel(
        basic = GetChannelResponse.ChannelBasic(
            timestamp = GetChannelResponse.Timestamp(
                publishedAt = mockPublishedAt
            )
        )
    )

    @Test
    fun `when get traffic summary is success, then it should return success`() {
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStats

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getChannelUseCase = mockGetChannelUseCase,
        )

        robot.use {
            val state = it.recordState {  }

            with(state) {
                liveReport.trafficMetricsResult.assertEqualTo(
                    NetworkResult.Success(
                        playBroadcastMapper.mapToLiveTrafficUiMetrics(mockLiveStats.channel.metrics)
                    )
                )
                channelSummary.date.assertEqualTo(mockPublishedAtFormatted)
                channelSummary.duration.assertEqualTo(mockLiveStats.duration)
                channelSummary.isEligiblePostVideo.assertTrue()
            }
        }
    }

    @Test
    fun `when get traffic summary failed, then it should return failed`() {
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } throws mockException

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getChannelUseCase = mockGetChannelUseCase,
        )

        robot.use {
            val state = it.recordState {  }

            with(state) {
                when(val result = liveReport.trafficMetricsResult) {
                    is NetworkResult.Fail -> {
                        assertTrue(result.error.message == mockException.message)
                        assertTrue(channelSummary.isEmpty())
                    }
                    else -> fail("Traffic metric result should be fail")
                }
            }
        }
    }

    @Test
    fun `when duration is less than 60 seconds, it wont be eligible to post video`() {
        val mockLiveStatUnder60Second = mockLiveStats.copy(
            duration = "00:00:10"
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStatUnder60Second

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getChannelUseCase = mockGetChannelUseCase,
        )

        robot.use {
            val state = it.recordState {  }

            with(state.channelSummary) {
                duration.assertEqualTo(mockLiveStatUnder60Second.duration)
                isEligiblePostVideo.assertFalse()
            }
        }
    }

    @Test
    fun `when parse duration is failed, it should not eligible to post video`() {
        val mockLiveStatWithWrongDuration = mockLiveStats.copy(
            duration = "wrong:duration:haha"
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStatWithWrongDuration

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getChannelUseCase = mockGetChannelUseCase,
        )

        robot.use {
            val state = it.recordState {  }

            with(state.channelSummary) {
                isEligiblePostVideo.assertFalse()
            }
        }
    }
}