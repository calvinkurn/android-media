package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 06/04/22
 */
@ExperimentalCoroutinesApi
class PlayUserReport {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val modelBuilder = ModelBuilder()

    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)

    @Test
    fun `when submit user report return success`(){
        val expectedResult = ResultState.Success
        coEvery { mockRepo.submitReport(any(), any(), any(), any(), any(), any()) } returns true

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            it.viewModel.submitUserReport(1L, "http://", 3L, 2, 5000L, "OKOKOKOKOK")
            val actualValue = it.viewModel.userReportSubmission.value
            actualValue.assertEqualTo(expectedResult)
        }
    }

    @Test
    fun `when submit user report return failed`(){
        coEvery { mockRepo.submitReport(any(), any(), any(), any(), any(), any()) } returns false

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            it.viewModel.submitUserReport(1L, "http://", 3L, 2, 5000L, "OKOKOKOKOK")
            val actualValue = it.viewModel.userReportSubmission.value.isFail
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
}