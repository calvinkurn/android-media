package com.tokopedia.play.broadcaster.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveCoachMark
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveInitState
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveState
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 21, 2022
 */
class PlayBroInteractiveFinishViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockLivePusher: PusherMediator = mockk(relaxed = true)
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockSharedPref: HydraSharedPreferences = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()
    private val interactiveUiModelBuilder = InteractiveUiModelBuilder()

    private val mockLeaderboardInfoResponse = interactiveUiModelBuilder.buildLeaderboardInfoModel()
    private val mockException = uiModelBuilder.buildException()

    @Test
    fun `when interactive is finished and has winners, it should emit value with coachmark`() {

        coEvery { mockRepo.getInteractiveLeaderboard(any(), any()) } returns mockLeaderboardInfoResponse

        val mockCoachMark = BroadcastInteractiveCoachMark.HasCoachMark("", "")
        val mockHasPrevious = BroadcastInteractiveInitState.HasPrevious(
            coachMark = mockCoachMark
        )
        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
        )

        val interactiveGqlLeaderboardDelay = robot.getInteractiveGqlLeaderboardDelay()

        rule.runBlockingTest {
            launch {
                robot.getViewModel().onInteractiveLiveEnded()
                advanceTimeBy(interactiveGqlLeaderboardDelay)
                val state = robot.getViewModel().observableInteractiveState.getOrAwaitValue()

                state.assertEqualTo(
                    BroadcastInteractiveState.Allowed.Init(mockHasPrevious)
                )
            }
        }
    }

    @Test
    fun `when interactive is finished but no winners, it should emit value with no coachmark`() {

        val mockCoachMark = BroadcastInteractiveCoachMark.NoCoachMark
        val mockHasPrevious = BroadcastInteractiveInitState.HasPrevious(mockCoachMark)
        val mockLeaderboardWithNoWinner = interactiveUiModelBuilder.buildLeaderboardInfoModel(
            leaderboardWinners = interactiveUiModelBuilder.buildLeaderboardWinnerList(1, 0)
        )
        coEvery { mockRepo.getInteractiveLeaderboard(any(), any()) } returns mockLeaderboardWithNoWinner

        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
        )

        val interactiveGqlLeaderboardDelay = robot.getInteractiveGqlLeaderboardDelay()

        rule.runBlockingTest {
            launch {
                robot.getViewModel().onInteractiveLiveEnded()
                advanceTimeBy(interactiveGqlLeaderboardDelay)
                val state = robot.getViewModel().observableInteractiveState.getOrAwaitValue()

                state.assertEqualTo(
                    BroadcastInteractiveState.Allowed.Init(mockHasPrevious)
                )
            }
        }
    }

    @Test
    fun `when interactive is finished but error when getting leaderboard, it should emit no previous state`() {

        val mockIsFirstInteractive = true
        val mockHasNoPrevious = BroadcastInteractiveInitState.NoPrevious(mockIsFirstInteractive)
        coEvery { mockSharedPref.isFirstInteractive() } returns mockIsFirstInteractive
        coEvery { mockRepo.getInteractiveLeaderboard(any(), any()) } throws mockException

        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
        )

        val interactiveGqlLeaderboardDelay = robot.getInteractiveGqlLeaderboardDelay()

        rule.runBlockingTest {
            launch {
                robot.getViewModel().onInteractiveLiveEnded()
                advanceTimeBy(interactiveGqlLeaderboardDelay)
                val state = robot.getViewModel().observableInteractiveState.getOrAwaitValue()

                state.assertEqualTo(
                    BroadcastInteractiveState.Allowed.Init(mockHasNoPrevious)
                )
            }
        }
    }
}