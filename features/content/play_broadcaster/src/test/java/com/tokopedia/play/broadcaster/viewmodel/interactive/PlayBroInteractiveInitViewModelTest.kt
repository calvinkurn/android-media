package com.tokopedia.play.broadcaster.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
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
    private val interactiveUiModelBuilder = InteractiveUiModelBuilder()

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
        coEvery { mockRepo.createInteractiveSession(any(), any(), any()) } returns mockInteractiveResponse

        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.getViewModel().createInteractiveSession(mockTitle, mockDurationInMs)

        val result = robot.getViewModel().observableCreateInteractiveSession.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(NetworkResult.Success::class.java)
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

        robot.getViewModel().createInteractiveSession("Giveaway", 1000L)

        val result = robot.getViewModel().observableCreateInteractiveSession.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(NetworkResult.Fail::class.java)
    }

    @Test
    fun `when user failed create new interactive session, it should return fail state`() {

        every { mockLivePusher.remainingDurationInMillis } returns 10_000
        coEvery { mockRepo.createInteractiveSession(any(), any(), any()) } throws mockException

        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.getViewModel().createInteractiveSession("Giveaway", 1000L)

        val result = robot.getViewModel().observableCreateInteractiveSession.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(NetworkResult.Fail::class.java)
    }
}