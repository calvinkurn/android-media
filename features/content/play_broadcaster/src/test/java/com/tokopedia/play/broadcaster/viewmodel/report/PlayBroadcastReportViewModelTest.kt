package com.tokopedia.play.broadcaster.viewmodel.report

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.report.BroadcasterReportUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.util.assertEqualTo
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
        coEvery { mockRepo.getReportProductSummary(any()) } returns mockProductReportSummary
    }

    @Test
    fun `playBroadcaster_report_getProductReportSummary_success`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val states = it.recordStateAsList {
                getAccountConfiguration()
                it.getViewModel().submitAction(PlayBroadcastAction.GetProductReportSummary)
            }

            states.last().productReportSummary.assertEqualTo(NetworkResult.Success(mockProductReportSummary))
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
