package com.tokopedia.play.broadcaster.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.millisFromNow
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
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
    private val mockInteractiveConfigInactiveResponse = interactiveUiModelBuilder.buildInteractiveConfigModel(
        giveawayConfig = interactiveUiModelBuilder.buildGiveawayConfig(isActive = false),
        quizConfig = interactiveUiModelBuilder.buildQuizConfig(isActive = false, showPrizeCoachMark = false),
    )
    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123"
    )
    private val mockInteractiveConfigResponse = interactiveUiModelBuilder.buildInteractiveConfigModel(
        quizConfig = interactiveUiModelBuilder.buildQuizConfig(
            showPrizeCoachMark = false,
        )
    )
    private val mockLivePusher = mockk<PusherMediator>(relaxed = true)


    init {
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        every { mockLivePusher.remainingDurationInMillis } returns 10000000L
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is inactive, it should emit interactive state forbidden`() {

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigInactiveResponse

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                getConfig()

                startLive()
            }

            state.interactiveConfig.assertEqualTo(mockInteractiveConfigInactiveResponse)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active & scheduled, it should emit interactive state scheduled`() {

        val mockTitle = "Giveaway Test"
        val mockTimeToStart = 0L.millisFromNow()
        val mockInteractiveDurationInMs = 0L.millisFromNow()
        val mockCurrentInteractive = interactiveUiModelBuilder.buildGiveaway(
            title = mockTitle,
            status = InteractiveUiModel.Giveaway.Status.Upcoming(
                startTime = mockTimeToStart,
                endTime = mockInteractiveDurationInMs,
            ),
        )

        coEvery { mockRepo.getChannelConfiguration() } returns mockk(relaxed = true)
        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockCurrentInteractive

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            livePusherMediator = mockLivePusher,
        )

        robot.use {
            val state = it.recordState {
                getConfig()

                startLive()
            }

            state.interactiveConfig.assertEqualTo(mockInteractiveConfigResponse)
            state.interactive.assertEqualTo(mockCurrentInteractive)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active & live, it should emit interactive state live`() {

        val mockRemainingTime = 1000L.millisFromNow()
        val mockCurrentInteractive = interactiveUiModelBuilder.buildGiveaway(
            status = InteractiveUiModel.Giveaway.Status.Ongoing(
                endTime = mockRemainingTime
            )
        )

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockCurrentInteractive

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            livePusherMediator = mockLivePusher,
        )

        robot.use {
            val state = it.recordState {
                getConfig()

                startLive()
            }

            state.interactiveConfig.assertEqualTo(mockInteractiveConfigResponse)
            state.interactive.assertEqualTo(mockCurrentInteractive)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active & finish, it should emit interactive finish state`() {

        val mockCurrentInteractive = interactiveUiModelBuilder.buildGiveaway(
            status = InteractiveUiModel.Giveaway.Status.Finished
        )

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockCurrentInteractive
        coEvery { mockRepo.getInteractiveLeaderboard(any(), any()) } returns mockLeaderboardInfoResponse

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            livePusherMediator = mockLivePusher,
        )

        robot.use {
            val state = it.recordState {
                getConfig()

                startLive()
            }

            state.interactiveConfig.assertEqualTo(mockInteractiveConfigResponse)
            state.interactive.assertEqualTo(mockCurrentInteractive)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active but the status is unknown, it should emit interactive with no previous state`() {
        val mockIsFirstInteractive = true
        val mockCurrentInteractive = interactiveUiModelBuilder.buildGiveaway(
            status = InteractiveUiModel.Giveaway.Status.Unknown
        )

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockCurrentInteractive
        coEvery { mockSharedPref.isFirstInteractive() } returns mockIsFirstInteractive

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
            livePusherMediator = mockLivePusher,
        )

        robot.use {
            val state = it.recordState {
                getConfig()

                startLive()
            }

            state.interactiveConfig.assertEqualTo(mockInteractiveConfigResponse)
            state.interactive.assertEqualTo(InteractiveUiModel.Unknown)
        }
    }

    @Test
    fun `when user starts livestreaming, get interactive config and interactive config is active but error happen, it should emit interactive with no previous state`() {
        val mockIsFirstInteractive = true

        coEvery { mockRepo.getInteractiveConfig() } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getCurrentInteractive(any()) } throws mockException
        coEvery { mockSharedPref.isFirstInteractive() } returns mockIsFirstInteractive

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
            livePusherMediator = mockLivePusher,
        )

        robot.use {
            val state = it.recordState {
                getConfig()

                startLive()
            }

            state.interactiveConfig.assertEqualTo(mockInteractiveConfigResponse)
            state.interactive.assertEqualTo(InteractiveUiModel.Unknown)
        }
    }
}