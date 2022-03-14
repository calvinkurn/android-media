package com.tokopedia.play.broadcaster.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 23, 2022
 */
class PlayBroInteractiveLeaderboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()
    private val interactiveUiModelBuilder = InteractiveUiModelBuilder()

    private val mockLeaderboardInfoResponse = interactiveUiModelBuilder.buildLeaderboardInfoModel()
    private val mockException = uiModelBuilder.buildException()

    @Test
    fun `when user successfully get leaderboard data, it should emit network result success along with leaderboard data`() {

        coEvery { mockRepo.getInteractiveLeaderboard(any(), any()) } returns mockLeaderboardInfoResponse

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            robot.getViewModel().getLeaderboardData()

            val result = robot.getViewModel().observableLeaderboardInfo.getOrAwaitValue()

            Assert.assertTrue(result is NetworkResult.Success)
            (result as NetworkResult.Success).data.assertEqualTo(mockLeaderboardInfoResponse)
        }
    }

    @Test
    fun `when user failed get leaderboard data, it should emit network result fail along with the exception`() {

        coEvery { mockRepo.getInteractiveLeaderboard(any(), any()) } throws mockException

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            robot.getViewModel().getLeaderboardData()

            val result = robot.getViewModel().observableLeaderboardInfo.getOrAwaitValue()

            Assert.assertTrue(result is NetworkResult.Fail)
            (result as NetworkResult.Fail).error.assertEqualTo(mockException)
        }
    }
}