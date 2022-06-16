package com.tokopedia.play.broadcaster.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 23, 2022
 */
class PlayBroInteractiveInitViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockLivePusher: PusherMediator = mockk(relaxed = true)
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockSharedPref: HydraSharedPreferences = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()

    private val mockException = uiModelBuilder.buildException()

    @Test
    fun `when user successfully create new interactive session, it should return interactive model`() {

        val mockTitle = "Giveaway"
        val mockDurationInMs = 1000L
        val mockInteractiveResponse = InteractiveSessionUiModel(
            id = "1",
            title = mockTitle,
            durationInMs = mockDurationInMs,
        )

        every { mockLivePusher.remainingDurationInMillis } returns 10_000
        coEvery { mockRepo.createGiveaway(any(), any(), any()) } returns mockInteractiveResponse

        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
        )
        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(PlayBroadcastAction.CreateGiveaway(mockTitle,mockDurationInMs))
            }
            events.last().assertEqualTo(PlayBroadcastEvent.CreateInteractive.Success(mockDurationInMs))
        }
    }

    @Test
    fun `when user creates new interactive session but the remaining duration is not enough, it should return fail state`() {
        every { mockLivePusher.remainingDurationInMillis } returns 500

        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(PlayBroadcastAction.CreateGiveaway("Giveaway",1000L))
            }
            Assertions.assertThat(events.last())
                .isInstanceOf(PlayBroadcastEvent.CreateInteractive.Error(mockException)::class.java)
        }
    }

    @Test
    fun `when user failed create new interactive session, it should return fail state`() {

        every { mockLivePusher.remainingDurationInMillis } returns 10_000
        coEvery { mockRepo.createGiveaway(any(), any(), any()) } throws mockException

        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
        )
        Assertions.assertThat(true)
        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(PlayBroadcastAction.CreateGiveaway("Giveaway",1000L))
            }
            Assertions.assertThat(events.last())
                .isInstanceOf(PlayBroadcastEvent.CreateInteractive.Error(mockException)::class.java)
        }
    }
}