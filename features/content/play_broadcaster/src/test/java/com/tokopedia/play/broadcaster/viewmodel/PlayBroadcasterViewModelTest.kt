package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on October 18, 2021
 */
class PlayBroadcasterViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val uiModelBuilder = UiModelBuilder()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given config info, when get live countdown duration, it should return duration stated on config info`() {
        val countDown = 5
        val configMock = uiModelBuilder.buildConfigurationUiModel(countDown = countDown.toLong())

        val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
        coEvery { mockRepo.getChannelConfiguration() } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.getConfig()
        robot.getViewModel().getBeforeLiveCountDownDuration().assertEqualTo(countDown)
    }

    @Test
    fun `given no config info, when get live countdown duration, it should return default duration`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher
        )

        val defaultCountDown = robot.getViewModelPrivateField<Int>("DEFAULT_BEFORE_LIVE_COUNT_DOWN")

        robot.getViewModel().getBeforeLiveCountDownDuration().assertEqualTo(defaultCountDown)
    }
}