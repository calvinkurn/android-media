package com.tokopedia.play.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.robot.play.andThen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.action.InteractiveOngoingFinishedAction
import com.tokopedia.play.view.uimodel.action.InteractivePreStartFinishedAction
import com.tokopedia.play.view.uimodel.state.PlayInteractiveUiState
import com.tokopedia.play_common.model.dto.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.PlayInteractiveTimeStatus
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
 * Created by jegul on 15/07/21
 */
/**
 * This test contains interactive test that starts from when we already have previous state of interactive
 */
class PlayLiveStepInteractiveTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData(
            channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.Live)
    )

    private val interactiveRepo: PlayViewerInteractiveRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given scheduled interactive, when timer finished, interactive should be changed to ongoing`() {
        val timeBeforeStartTap = 15000L
        val durationTap = 5000L
        val title = "Giveaway"
        coEvery { interactiveRepo.getActiveInteractiveId() } returns "1"
        coEvery { interactiveRepo.getDetail(any()) } returns PlayCurrentInteractiveModel(
                timeStatus = PlayInteractiveTimeStatus.Scheduled(timeBeforeStartTap, durationTap),
                title = title
        )

        givenPlayViewModelRobot(
                interactiveRepo = interactiveRepo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.andThen {
            viewModel.submitAction(InteractivePreStartFinishedAction)
        }.thenVerify {
            withState {
                interactive.isEqualTo(
                        PlayInteractiveUiState.Ongoing(durationTap)
                )
            }
        }
    }

    @Test
    fun `given live interactive, when timer finished, interactive should be changed to finished`() {
        val durationTap = 5000L
        val title = "Giveaway"
        coEvery { interactiveRepo.getActiveInteractiveId() } returns "1"
        coEvery { interactiveRepo.getDetail(any()) } returns PlayCurrentInteractiveModel(
                timeStatus = PlayInteractiveTimeStatus.Live(durationTap),
                title = title
        )

        givenPlayViewModelRobot(
                interactiveRepo = interactiveRepo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.andThen {
            viewModel.submitAction(InteractiveOngoingFinishedAction)
        }.thenVerify {
            withState {
                interactive.isEqualTo(
                        PlayInteractiveUiState.Finished(com.tokopedia.play.R.string.play_interactive_finish_initial_text)
                )
            }
        }
    }
}