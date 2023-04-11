package com.tokopedia.play.broadcaster.tnc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
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
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockTnCList = List(3) {
        uiModelBuilder.buildTermsAndConditionUiModel(it.toString())
    }
    private val mockConfigTrue = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
    )
    private val mockConfigFalse = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = false,
        tnc = mockTnCList,
    )

    @Before
    fun setUp() {
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns uiModelBuilder.buildBroadcastingConfigUiModel()
        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
    }

    @Test
    fun `given seller is not allowed to stream, when get configuration, then seller cannot stream`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfigFalse

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
            }

            state.channel
                .streamAllowed
                .assertEqualTo(false)

            state.channel
                .tnc
                .assertEqualTo(mockTnCList)
        }
    }

    @Test
    fun `given seller is allowed to stream, when get configuration, then seller can stream`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfigTrue

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
            }

            state.channel
                .streamAllowed
                .assertEqualTo(true)
        }
    }
}
