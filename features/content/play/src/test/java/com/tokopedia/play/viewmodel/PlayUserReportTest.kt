package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertInstanceOf
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.action.OpenFooterUserReport
import com.tokopedia.play.view.uimodel.action.OpenKebabAction
import com.tokopedia.play.view.uimodel.action.OpenUserReport
import com.tokopedia.play.view.uimodel.action.SelectReason
import com.tokopedia.play.view.uimodel.event.OpenKebabEvent
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play.view.uimodel.event.OpenUserReportEvent
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 06/04/22
 */
@ExperimentalCoroutinesApi
class PlayUserReportTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val testDispatcher = coroutineTestRule.dispatchers

    private val modelBuilder = ModelBuilder()

    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()

    private val mockLiveChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(
                channelType = PlayChannelType.Live
            )
        )
    )

    private val mockException = Exception("Something went wrong.")

    @Test
    fun `when click kebab button hit analytic`(){
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        ){
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
        }

        robot.use {
            it.recordEvent {
                submitAction(OpenKebabAction)
            }.last().assertEqualTo(OpenKebabEvent)
        }
    }

    @Test
    fun `when click footer open page based on applink`(){
        val appLink = "tokopedia://play/12668"
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        ){
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
        }

        robot.use {
            it.recordEvent {
                submitAction(OpenFooterUserReport(appLink))
            }.last().assertEqualTo(OpenPageEvent(appLink))
        }
    }

    @Test
    fun `when click report if user not logged in, go to login page`(){
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        ){
            setLoggedIn(false)
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
        }

        robot.use {
            it.recordEvent {
                submitAction(OpenUserReport)
            }.last().assertEqualTo(OpenPageEvent(applink = ApplinkConst.LOGIN, requestCode = 575)) //Request code for user report [REQUEST_CODE_USER_REPORT]
        }
    }

    @Test
    fun `when click report if user is logged in, go to user report list`(){
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        ){
            setLoggedIn(true)
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
        }

        robot.use {
            it.recordEvent {
                submitAction(OpenUserReport)
            }.last().assertEqualTo(OpenUserReportEvent)
        }
    }

    @Test
    fun `when submit user report return success`(){
        val expectedResult = ResultState.Success
        coEvery { mockRepo.submitReport(any(), any(), any(), any(), any(), any(), any()) } returns true

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            it.viewModel.submitUserReport(mediaUrl = "http://play", reasonId = 1, timestamp = 90L, reportDesc = "hihi")
            val actualValue = it.viewModel.userReportSubmission.value
            actualValue.state.assertEqualTo(expectedResult)
        }
    }

    @Test
    fun `when submit user report return failed`(){
        coEvery { mockRepo.submitReport(any(), any(), any(), any(), any(), any(), any()) } returns false

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            it.viewModel.submitUserReport(mediaUrl = "http://play", reasonId = 1, timestamp = 90L, reportDesc = "hihi")
            val actualValue = it.viewModel.userReportSubmission.value.state.isFail
            actualValue.assertTrue()
        }
    }

    @Test
    fun `when get reasoning list is success`(){
        val expectedResult = modelBuilder.buildUserReportList().reasoningList
        coEvery { mockRepo.getReasoningList() } returns expectedResult

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            it.viewModel.getUserReportList()
            val actualValue = it.viewModel.userReportItems.value
            actualValue.resultState.assertEqualTo(ResultState.Success)
            actualValue.reasoningList.assertEqualTo(expectedResult)
        }
    }

    @Test
    fun `when get reasoning list is error`(){
        coEvery { mockRepo.getReasoningList() } throws mockException

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            it.viewModel.getUserReportList()
            val actualValue = it.viewModel.userReportItems.value
            actualValue.resultState.assertInstanceOf<ResultState.Fail>()
            actualValue.reasoningList.assertEqualTo(emptyList())
        }
    }

    @Test
    fun `when user click item report select from reasoning list`(){
        val expectedResult = modelBuilder.buildUserReportList().reasoningList
        coEvery { mockRepo.getReasoningList() } returns expectedResult

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        val reasonId = 1

        robot.use {
            it.viewModel.getUserReportList()
            val actualValue = it.viewModel.userReportItems.value
            actualValue.resultState.assertEqualTo(ResultState.Success)
            actualValue.reasoningList.assertEqualTo(expectedResult)

            it.viewModel.submitAction(SelectReason(reasonId))
            val filtered = expectedResult.filterIsInstance<PlayUserReportReasoningUiModel.Reasoning>().find {  it.reasoningId == reasonId }
            it.viewModel.userReportSubmission.value.selectedReasoning.assertEqualTo(filtered)
        }
    }
}
