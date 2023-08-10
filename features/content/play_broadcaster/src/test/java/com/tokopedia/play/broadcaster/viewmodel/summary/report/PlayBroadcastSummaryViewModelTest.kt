package com.tokopedia.play.broadcaster.viewmodel.summary.report

import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.GetSellerLeaderboardSlotResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveSummaryLivestreamResponse
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveSummaryLivestreamUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetSellerLeaderboardUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastSummaryViewModelRobot
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricType
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.util.*
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
                channelSummary.isEligiblePostVideo.assertTrue()
            }
        }
    }

    @Test
    fun `when get traffic summary is success & theres a leaderboard, then it should return success with additional metrics`() {
        val mockTotalInteractiveParticipant = "0"
        val mockMetricList = mutableListOf(
            TrafficMetricUiModel(
                type = TrafficMetricType.GameParticipants,
                count = mockTotalInteractiveParticipant
            )
        ).apply {
            addAll(playBroadcastMapper.mapToLiveTrafficUiMetrics(TYPE_SHOP, mockLiveStats))
        }

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

    @Test
    fun `when duration is less than 60 seconds, it wont be eligible to post video`() {
        val mockLiveStatUnder60Second = mockLiveStats.copy(
            duration = "00:00:59"
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStatUnder60Second
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns mockSlotResponse
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns mockParticipant
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase
        )

        robot.use {
            val (state, events) = it.recordStateAndEvent { }

            with(state.channelSummary) {
                duration.assertEqualTo(mockLiveStatUnder60Second.duration)
                isEligiblePostVideo.assertFalse()
            }

            Assertions.assertThat(events.last()).isInstanceOf(PlayBroadcastSummaryEvent.VideoUnder60Seconds::class.java)
        }
    }

    @Test
    fun `when duration is exactly 60 second, it should be eligible to post video`() {
        val mockLiveStat60Second = mockLiveStats.copy(
            duration = "00:01:00"
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStat60Second
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns mockSlotResponse
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns mockParticipant
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase
        )

        robot.use {
            val state = it.recordState { }

            with(state.channelSummary) {
                duration.assertEqualTo(mockLiveStat60Second.duration)
                isEligiblePostVideo.assertTrue()
            }
        }
    }

    @Test
    fun `when duration is exactly 1 hour, it should be eligible to post video`() {
        val mockLiveStatExact1Hour = mockLiveStats.copy(
            duration = "01:00:00"
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStatExact1Hour
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns mockSlotResponse
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns mockParticipant
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase
        )

        robot.use {
            val state = it.recordState { }

            with(state.channelSummary) {
                duration.assertEqualTo(mockLiveStatExact1Hour.duration)
                isEligiblePostVideo.assertTrue()
            }
        }
    }

    @Test
    fun `when duration is in mmss format and below, it should be eligible to post video`() {
        val mockLiveStat = mockLiveStats.copy(
            duration = "00:10"
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStat
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns mockSlotResponse
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns mockParticipant
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase
        )

        robot.use {
            val (state, events) = it.recordStateAndEvent { }

            with(state.channelSummary) {
                duration.assertEqualTo(mockLiveStat.duration)
                isEligiblePostVideo.assertFalse()
            }

            Assertions.assertThat(events.last()).isInstanceOf(PlayBroadcastSummaryEvent.VideoUnder60Seconds::class.java)
        }
    }

    @Test
    fun `when duration is in ss format and below, it should be eligible to post video`() {
        val mockLiveStat = mockLiveStats.copy(
            duration = "10"
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStat
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns mockSlotResponse
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns mockParticipant
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase
        )

        robot.use {
            val (state, events) = it.recordStateAndEvent { }

            with(state.channelSummary) {
                duration.assertEqualTo(mockLiveStat.duration)
                isEligiblePostVideo.assertFalse()
            }

            Assertions.assertThat(events.last()).isInstanceOf(PlayBroadcastSummaryEvent.VideoUnder60Seconds::class.java)
        }
    }

    @Test
    fun `when parse duration is failed, it should not eligible to post video`() {
        val mockLiveStatWithWrongDuration = mockLiveStats.copy(
            duration = "wrong:duration:haha"
        )

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns mockLiveStatWithWrongDuration
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns mockSlotResponse
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns mockParticipant
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase
        )

        robot.use {
            val state = it.recordState { }

            with(state.channelSummary) {
                isEligiblePostVideo.assertFalse()
            }
        }
    }
}
