package com.tokopedia.play.broadcaster.tnc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 06/10/21
 */
class PlayBroadcastTnCTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val uiModelBuilder = UiModelBuilder()

    @Test
    fun `given seller is not allowed to stream, when get configuration, then seller cannot stream`() {
        val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
        val mockTnCList = List(3) {
            uiModelBuilder.buildTermsAndConditionUiModel(it.toString())
        }
        val mockConfig = uiModelBuilder.buildConfigurationUiModel(
            streamAllowed = false,
            tnc = mockTnCList
        )
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
            }

            state.channel
                .canStream
                .assertEqualTo(false)

            state.channel
                .tnc
                .assertEqualTo(mockTnCList)
        }
    }

    @Test
    fun `given seller is allowed to stream, when get configuration, then seller can stream`() {
        val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

        val mockConfig = uiModelBuilder.buildConfigurationUiModel(
            streamAllowed = true,
        )
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
            }

            state.channel
                .canStream
                .assertEqualTo(true)
        }
    }
}