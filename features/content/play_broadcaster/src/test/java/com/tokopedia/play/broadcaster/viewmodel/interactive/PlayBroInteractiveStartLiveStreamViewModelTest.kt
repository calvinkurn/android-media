package com.tokopedia.play.broadcaster.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveCoachMark
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveInitState
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveState
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 23, 2022
 */
class PlayBroInteractiveStartLiveStreamViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockSharedPref: HydraSharedPreferences = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()
    private val interactiveUiModelBuilder = InteractiveUiModelBuilder()

    private val mockLeaderboardInfoResponse = interactiveUiModelBuilder.buildLeaderboardInfoModel()
    private val mockException = uiModelBuilder.buildException()
    private val mockInteractiveConfigInactiveResponse = interactiveUiModelBuilder.buildInteractiveConfigModel(isActive = false)
    private val mockInteractiveConfigResponse = interactiveUiModelBuilder.buildInteractiveConfigModel()

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is inactive, it should emit interactive state forbidden`() {

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigInactiveResponse

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            robot.getViewModel().startLiveStream(true)

            val configResult = robot.getViewModel().observableInteractiveConfig.getOrAwaitValue()
            val stateResult = robot.getViewModel().observableInteractiveState.getOrAwaitValue()

            configResult.assertEqualTo(mockInteractiveConfigInactiveResponse)
            stateResult.assertEqualTo(BroadcastInteractiveState.Forbidden)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active & scheduled, it should emit interactive state scheduled`() {

        val mockTitle = "Giveaway Test"
        val mockTimeToStart = 0L
        val mockInteractiveDurationInMs = 0L
        val mockCurrentInteractive = interactiveUiModelBuilder.buildCurrentInteractiveModel(
            title = mockTitle,
            timeStatus = PlayInteractiveTimeStatus.Scheduled(
                timeToStartInMs = mockTimeToStart,
                interactiveDurationInMs = mockInteractiveDurationInMs,
            )
        )

        val mockExpectedState = BroadcastInteractiveState.Allowed.Schedule(
            timeToStartInMs = mockTimeToStart,
            durationInMs = mockInteractiveDurationInMs,
            title = mockTitle,
        )

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockCurrentInteractive

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            robot.getViewModel().startLiveStream(true)

            val configResult = robot.getViewModel().observableInteractiveConfig.getOrAwaitValue()
            val stateResult = robot.getViewModel().observableInteractiveState.getOrAwaitValue()

            configResult.assertEqualTo(mockInteractiveConfigResponse)
            stateResult.assertEqualTo(mockExpectedState)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active & live, it should emit interactive state live`() {

        val mockRemainingTime = 1000L
        val mockCurrentInteractive = interactiveUiModelBuilder.buildCurrentInteractiveModel(
            timeStatus = PlayInteractiveTimeStatus.Live(
                remainingTimeInMs = mockRemainingTime
            )
        )

        val mockExpectedState = BroadcastInteractiveState.Allowed.Live(
            remainingTimeInMs = mockRemainingTime,
        )

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockCurrentInteractive

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            robot.getViewModel().startLiveStream(true)

            val configResult = robot.getViewModel().observableInteractiveConfig.getOrAwaitValue()
            val stateResult = robot.getViewModel().observableInteractiveState.getOrAwaitValue()

            configResult.assertEqualTo(mockInteractiveConfigResponse)
            stateResult.assertEqualTo(mockExpectedState)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active & finish, it should emit interactive finish state`() {

        val mockCurrentInteractive = interactiveUiModelBuilder.buildCurrentInteractiveModel(
            timeStatus = PlayInteractiveTimeStatus.Finished
        )

        val mockCoachMark = BroadcastInteractiveCoachMark.HasCoachMark("", "")
        val mockHasPrevious = BroadcastInteractiveInitState.HasPrevious(
            coachMark = mockCoachMark
        )
        val mockExpectedState = BroadcastInteractiveState.Allowed.Init(
            state = mockHasPrevious
        )

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockCurrentInteractive
        coEvery { mockRepo.getInteractiveLeaderboard(any(), any()) } returns mockLeaderboardInfoResponse

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        val interactiveGqlLeaderboardDelay = robot.getInteractiveGqlLeaderboardDelay()

        rule.runBlockingTest {
            robot.getViewModel().startLiveStream(true)
            advanceTimeBy(interactiveGqlLeaderboardDelay)

            val configResult = robot.getViewModel().observableInteractiveConfig.getOrAwaitValue()
            val stateResult = robot.getViewModel().observableInteractiveState.getOrAwaitValue()

            configResult.assertEqualTo(mockInteractiveConfigResponse)
            stateResult.assertEqualTo(mockExpectedState)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active but the status is unknown, it should emit interactive with no previous state`() {
        val mockIsFirstInteractive = true
        val mockCurrentInteractive = interactiveUiModelBuilder.buildCurrentInteractiveModel(
            timeStatus = PlayInteractiveTimeStatus.Unknown
        )

        val mockExpectedState = BroadcastInteractiveState.Allowed.Init(
            state = BroadcastInteractiveInitState.NoPrevious(mockIsFirstInteractive)
        )

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockCurrentInteractive
        coEvery { mockSharedPref.isFirstInteractive() } returns mockIsFirstInteractive

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref
        )

        robot.use {
            robot.getViewModel().startLiveStream(true)

            val configResult = robot.getViewModel().observableInteractiveConfig.getOrAwaitValue()
            val stateResult = robot.getViewModel().observableInteractiveState.getOrAwaitValue()

            configResult.assertEqualTo(mockInteractiveConfigResponse)
            stateResult.assertEqualTo(mockExpectedState)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active but error happen, it should emit interactive with no previous state`() {
        val mockIsFirstInteractive = true
        val mockExpectedState = BroadcastInteractiveState.Allowed.Init(
            state = BroadcastInteractiveInitState.NoPrevious(mockIsFirstInteractive)
        )

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } throws mockException
        coEvery { mockSharedPref.isFirstInteractive() } returns mockIsFirstInteractive

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref
        )

        robot.use {
            robot.getViewModel().startLiveStream(true)

            val configResult = robot.getViewModel().observableInteractiveConfig.getOrAwaitValue()
            val stateResult = robot.getViewModel().observableInteractiveState.getOrAwaitValue()

            configResult.assertEqualTo(mockInteractiveConfigResponse)
            stateResult.assertEqualTo(mockExpectedState)
        }
    }
}