package com.tokopedia.play.broadcaster.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
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

    init {
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
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

}