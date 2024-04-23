package com.tokopedia.play.broadcaster.viewmodel.summary.report

import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.GetSellerLeaderboardSlotResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveSummaryLivestreamResponse
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveSummaryLivestreamUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetSellerLeaderboardUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastSummaryViewModelRobot
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricType
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.play.broadcaster.util.TestUriParser
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
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

    private val playBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer(), TestUriParser(), mockk(relaxed = true))

    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetLiveStatisticsUseCase: GetLiveStatisticsUseCase = mockk(relaxed = true)
    private val mockGetSellerLeaderboardUseCase: GetSellerLeaderboardUseCase = mockk(relaxed = true)
    private val mockGetInteractiveSummaryLivestreamUseCase: GetInteractiveSummaryLivestreamUseCase = mockk(relaxed = true)
    private val hydraConfigStore: HydraConfigStore = mockk(relaxed = true)

    private val modelBuilder = UiModelBuilder()
    private val mockException = modelBuilder.buildException()
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
    private val mockSlotResponse = GetSellerLeaderboardSlotResponse()
    private val mockParticipant = GetInteractiveSummaryLivestreamResponse(
        GetInteractiveSummaryLivestreamResponse.PlayInteractiveGetSummaryLivestream(0)
    )

    @Test
    fun `when get traffic summary is success, then it should return success`() {
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStats
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns mockSlotResponse
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns mockParticipant
        coEvery { hydraConfigStore.getAuthorType() } returns TYPE_SHOP
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getChannelUseCase = mockGetChannelUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase,
            hydraConfigStore = hydraConfigStore
        )

        robot.use {
            val state = it.recordState { }

            with(state) {
                liveReport.trafficMetricsResult.assertEqualTo(
                    NetworkResult.Success(
                        playBroadcastMapper.mapToLiveTrafficUiMetrics(TYPE_SHOP, mockLiveStats)
                    )
                )
                channelSummary.date.assertEqualTo(mockPublishedAtFormatted)
                channelSummary.duration.assertEqualTo(mockLiveStats.duration)
            }
        }
    }

    @Test
    fun `when get traffic summary is success & theres a leaderboard, then it should return success with additional metrics`() {
        val mockTotalInteractiveParticipant = "0"
        val mockMetricHighlightList = listOf(
            LiveStatsUiModel.GameParticipant(mockTotalInteractiveParticipant)
        )

        val mockMetricList = playBroadcastMapper.mapToLiveTrafficUiMetrics(TYPE_SHOP, mockLiveStats)

        val mockSlotResponse = mockSlotResponse.copy(
            data = GetSellerLeaderboardSlotResponse.Data(slots = listOf(GetSellerLeaderboardSlotResponse.SlotData()))
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStats
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns mockSlotResponse
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns mockParticipant
        coEvery { hydraConfigStore.getAuthorType() } returns TYPE_SHOP
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getChannelUseCase = mockGetChannelUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase,
            hydraConfigStore = hydraConfigStore
        )

        robot.use {
            val state = it.recordState { }

            with(state) {
                liveReport.trafficMetricsResult.assertEqualTo(
                    NetworkResult.Success(mockMetricList)
                )
                liveReport.trafficMetricHighlight.assertEqualTo(
                    NetworkResult.Success(mockMetricHighlightList)
                )
                channelSummary.date.assertEqualTo(mockPublishedAtFormatted)
                channelSummary.duration.assertEqualTo(mockLiveStats.duration)
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
            getChannelUseCase = mockGetChannelUseCase
        )

        robot.use {
            val state = it.recordState { }

            with(state) {
                when (val result = liveReport.trafficMetricsResult) {
                    is NetworkResult.Fail -> {
                        assertTrue(result.error.message == mockException.message)
                        assertTrue(channelSummary.isEmpty())
                    }
                    else -> fail("Traffic metric result should be fail")
                }
            }
        }
    }
}
