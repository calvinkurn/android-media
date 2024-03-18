package com.tokopedia.play.broadcaster.viewmodel.report

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.report.BroadcasterReportUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertType
import com.tokopedia.play.broadcaster.util.assertWhenFailed
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Jonathan Darwin on 18 March 2024
 */
class PlayBroadcastReportViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val uiModelBuilder = UiModelBuilder()
    private val reportModelBuilder = BroadcasterReportUiModelBuilder()

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    private val mockException = uiModelBuilder.buildException()
    private val mockLiveReportSummary = reportModelBuilder.buildLiveReportSummary()
    private val mockProductReportSummary = reportModelBuilder.buildProductReportSummary()
    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123"
    )

    @Before
    fun setUp() {
        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns uiModelBuilder.buildBroadcastingConfigUiModel()

        coEvery { mockRepo.getReportSummary(any(), any()) } returns mockLiveReportSummary
        coEvery { mockRepo.getReportProductSummary(any()) } returns mockProductReportSummary
    }

    @Test
    fun `playBroadcaster_report_getReportSummary_success`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                it.getViewModel().submitAction(PlayBroadcastAction.GetLiveReportSummary)
            }

            state.liveReportSummary.liveStats[0].assertType<LiveStatsUiModel.Viewer> {
                it.value.assertEqualTo(mockLiveReportSummary.liveStats.filterIsInstance<LiveStatsUiModel.Viewer>().first().value)
            }

            state.liveReportSummary.liveStats[1].assertType<LiveStatsUiModel.TotalViewer> {
                it.value.assertEqualTo(mockLiveReportSummary.liveStats.filterIsInstance<LiveStatsUiModel.TotalViewer>().first().value)
            }

            state.liveReportSummary.liveStats[2].assertType<LiveStatsUiModel.EstimatedIncome> {
                it.value.assertEqualTo(mockLiveReportSummary.liveStats.filterIsInstance<LiveStatsUiModel.EstimatedIncome>().first().value)
            }

            state.liveReportSummary.liveStats[3].assertType<LiveStatsUiModel.Like> {
                it.value.assertEqualTo(mockLiveReportSummary.liveStats.filterIsInstance<LiveStatsUiModel.Like>().first().value)
            }
        }
    }

    @Test
    fun `playBroadcaster_report_getReportSummary_error`() {
        coEvery { mockRepo.getReportSummary(any(), any()) } throws mockException

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val prevState = it.recordState {
                getAccountConfiguration()
            }

            val currState = it.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.GetLiveReportSummary)
            }

            currState.liveReportSummary.assertEqualTo(prevState.liveReportSummary)
        }
    }

    @Test
    fun `playBroadcaster_report_getReportSummary_raceCondition`() {

        val mockPrevLiveReportSummary = reportModelBuilder.buildLiveReportSummary(
            estimatedIncome = "2",
            viewer = "2",
            totalViewer = "2",
            like = "2",
            timestamp = "2"
        )
        val mockCurrLiveReportSummary = reportModelBuilder.buildLiveReportSummary(
            estimatedIncome = "1",
            viewer = "1",
            totalViewer = "1",
            like = "1",
            timestamp = "1"
        )

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {

            coEvery { mockRepo.getReportSummary(any(), any()) } returns mockPrevLiveReportSummary

            val prevState = it.recordState {
                getAccountConfiguration()
                it.getViewModel().submitAction(PlayBroadcastAction.GetLiveReportSummary)
            }

            coEvery { mockRepo.getReportSummary(any(), any()) } returns mockCurrLiveReportSummary

            val currState = it.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.GetLiveReportSummary)
            }

            currState.liveReportSummary.liveStats[0].assertType<LiveStatsUiModel.Viewer> {
                it.value.assertEqualTo(prevState.liveReportSummary.liveStats.filterIsInstance<LiveStatsUiModel.Viewer>().first().value)
            }

            currState.liveReportSummary.liveStats[1].assertType<LiveStatsUiModel.TotalViewer> {
                it.value.assertEqualTo(prevState.liveReportSummary.liveStats.filterIsInstance<LiveStatsUiModel.TotalViewer>().first().value)
            }

            currState.liveReportSummary.liveStats[2].assertType<LiveStatsUiModel.EstimatedIncome> {
                it.value.assertEqualTo(prevState.liveReportSummary.liveStats.filterIsInstance<LiveStatsUiModel.EstimatedIncome>().first().value)
            }

            currState.liveReportSummary.liveStats[3].assertType<LiveStatsUiModel.Like> {
                it.value.assertEqualTo(prevState.liveReportSummary.liveStats.filterIsInstance<LiveStatsUiModel.Like>().first().value)
            }

            currState.liveReportSummary.timestamp.assertEqualTo(prevState.liveReportSummary.timestamp)
        }
    }

    @Test
    fun `playBroadcaster_report_getProductReportSummary_success`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                it.getViewModel().submitAction(PlayBroadcastAction.GetProductReportSummary)
            }

            state.productReportSummary.assertEqualTo(NetworkResult.Success(mockProductReportSummary))
        }
    }

    @Test
    fun `playBroadcaster_report_getProductReportSummary_error`() {
        coEvery { mockRepo.getReportProductSummary(any()) } throws mockException

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val states = it.recordStateAsList {
                getAccountConfiguration()
                it.getViewModel().submitAction(PlayBroadcastAction.GetProductReportSummary)
            }

            states.last().productReportSummary.assertWhenFailed { throwable ->
                throwable.assertEqualTo(mockException)
            }
        }
    }
}
