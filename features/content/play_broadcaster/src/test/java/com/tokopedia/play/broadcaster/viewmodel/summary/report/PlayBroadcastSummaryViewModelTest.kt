package com.tokopedia.play.broadcaster.viewmodel.summary.report

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastSummaryViewModelRobot
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.LiveDurationUiModel
import com.tokopedia.play.broadcaster.util.*
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
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

    private val mockGetLiveStatisticsUseCase: GetLiveStatisticsUseCase = mockk(relaxed = true)

    private val modelBuilder = UiModelBuilder()
    private val mockException = Exception("Network Error")
    private val mockLiveStats by lazy { modelBuilder.buildLiveStats() }

    @Test
    fun `when get traffic summary is success, then it should return success`() {
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStats

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
        )

        robot.use {
            val state = it.recordState {  }

            with(state.liveReport) {
                trafficMetricsResult.assertEqualTo(
                    NetworkResult.Success(
                        playBroadcastMapper.mapToLiveTrafficUiMetrics(mockLiveStats.channel.metrics)
                    )
                )
                duration.duration.assertEqualTo(mockLiveStats.duration)
                duration.isEligiblePostVideo.assertTrue()
            }
        }
    }

    @Test
    fun `when get traffic summary failed, then it should return failed`() {
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } throws mockException

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
        )

        robot.use {
            val state = it.recordState {  }

            with(state.liveReport) {
                when(val result = trafficMetricsResult) {
                    is NetworkResult.Fail -> {
                        assertTrue(result.error.message == mockException.message)
                        duration.assertEqualTo(LiveDurationUiModel.empty())
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

        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStatUnder60Second

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
        )

        robot.use {
            val state = it.recordState {  }

            with(state.liveReport) {
                duration.duration.assertEqualTo(mockLiveStatUnder60Second.duration)
                duration.isEligiblePostVideo.assertFalse()
            }
        }
    }
}