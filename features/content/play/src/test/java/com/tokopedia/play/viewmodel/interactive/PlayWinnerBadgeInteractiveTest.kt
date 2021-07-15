package com.tokopedia.play.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.data.websocket.PlayChannelWebSocket
import com.tokopedia.play.data.websocket.revamp.WebSocketAction
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.extensions.isLeaderboardSheetShown
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.model.PlayInteractiveModelBuilder
import com.tokopedia.play.model.PlaySocketResponseBuilder
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.action.ClickCloseLeaderboardSheetAction
import com.tokopedia.play.view.uimodel.action.InteractiveWinnerBadgeClickedAction
import com.tokopedia.play.view.uimodel.state.PlayInteractiveUiState
import com.tokopedia.play_common.model.dto.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.PlayInteractiveTimeStatus
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 15/07/21
 */
class PlayWinnerBadgeInteractiveTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val socketResponseBuilder = PlaySocketResponseBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData(
            channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.Live)
    )

    private val interactiveModelBuilder = PlayInteractiveModelBuilder()

    private val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                    socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
            )
    )
    private val socket: PlayChannelWebSocket = mockk(relaxed = true)

    private val interactiveRepo: PlayViewerInteractiveRepository = mockk(relaxed = true)

    init {
        every { socket.listenAsFlow() } returns socketFlow
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given has leaderboard, when interactive is finished, there should be winner badge shown`() {
        val title = "Giveaway"
        coEvery { interactiveRepo.getCurrentInteractive(any()) } returns PlayCurrentInteractiveModel(
                timeStatus = PlayInteractiveTimeStatus.Finished,
                title = title
        )
        coEvery { interactiveRepo.getInteractiveLeaderboard(any()) } returns interactiveModelBuilder.buildLeaderboardInfo(
                leaderboardWinners = listOf(interactiveModelBuilder.buildLeaderboard())
        )

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                interactiveRepo = interactiveRepo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactive.isEqualTo(
                        PlayInteractiveUiState.NoInteractive
                )
                showWinnerBadge.isTrue()
            }
        }
    }

    @Test
    fun `given leaderboard error, when interactive is finished, there should be no badge`() {
        val title = "Giveaway"
        coEvery { interactiveRepo.getCurrentInteractive(any()) } returns PlayCurrentInteractiveModel(
                timeStatus = PlayInteractiveTimeStatus.Finished,
                title = title
        )
        coEvery { interactiveRepo.getInteractiveLeaderboard(any()) } throws IllegalArgumentException("abc")

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                interactiveRepo = interactiveRepo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactive.isEqualTo(
                        PlayInteractiveUiState.NoInteractive
                )
                showWinnerBadge.isFalse()
            }
        }
    }

    @Test
    fun `given no leaderboard winners, when retrieved, there should be no badge`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
                WebSocketAction.NewMessage(
                        socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
                )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val interactiveRepo: PlayViewerInteractiveRepository = mockk(relaxed = true)
        val title = "Giveaway"
        coEvery { interactiveRepo.getCurrentInteractive(any()) } returns PlayCurrentInteractiveModel(
                timeStatus = PlayInteractiveTimeStatus.Finished,
                title = title
        )
        coEvery { interactiveRepo.getInteractiveLeaderboard(any()) } returns interactiveModelBuilder.buildLeaderboardInfo(
                leaderboardWinners = emptyList()
        )

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                interactiveRepo = interactiveRepo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactive.isEqualTo(
                        PlayInteractiveUiState.NoInteractive
                )
                showWinnerBadge.isFalse()
            }
        }
    }

    @Test
    fun `given winner badge is shown, when click winner badge action, then leaderboard bottom sheet should be shown`() {
        val title = "Giveaway"
        coEvery { interactiveRepo.getCurrentInteractive(any()) } returns PlayCurrentInteractiveModel(
                timeStatus = PlayInteractiveTimeStatus.Finished,
                title = title
        )
        coEvery { interactiveRepo.getInteractiveLeaderboard(any()) } returns interactiveModelBuilder.buildLeaderboardInfo(
                leaderboardWinners = listOf(interactiveModelBuilder.buildLeaderboard())
        )

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                interactiveRepo = interactiveRepo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.andWhen {
            viewModel.submitAction(InteractiveWinnerBadgeClickedAction(10))
        }.thenVerify {
            withState {
                bottomInsets.isLeaderboardSheetShown.isTrue()
            }
        }.andWhen {
            viewModel.submitAction(ClickCloseLeaderboardSheetAction)
        }.thenVerify {
            withState {
                bottomInsets.isLeaderboardSheetShown.isFalse()
            }
        }
    }
}