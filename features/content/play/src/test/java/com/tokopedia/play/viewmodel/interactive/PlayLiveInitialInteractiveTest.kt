package com.tokopedia.play.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.data.websocket.PlayChannelWebSocket
import com.tokopedia.play.data.websocket.revamp.WebSocketAction
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.model.PlayInteractiveModelBuilder
import com.tokopedia.play.model.PlaySocketResponseBuilder
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.type.PlayChannelType
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
import java.lang.IllegalArgumentException

/**
 * Created by jegul on 15/07/21
 */
/**
 * This test contains interactive test that starts from when we received socket that tells us
 * about the status of the interactive
 */
class PlayLiveInitialInteractiveTest {

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

    private val socket: PlayChannelWebSocket = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given no active interactive, when retrieved, there should be no interactive`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
                WebSocketAction.NewMessage(
                        socketResponseBuilder.buildChannelInteractiveResponse(isExist = false)
                )
        )
        every { socket.listenAsFlow() } returns socketFlow

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactive.isEqualTo(
                        PlayInteractiveUiState.NoInteractive
                )
            }
        }
    }

    @Test
    fun `given has active scheduled interactive, when retrieved, state should be prestart`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
                WebSocketAction.NewMessage(
                        socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
                )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val interactiveRepo: PlayViewerInteractiveRepository = mockk(relaxed = true)
        val timeBeforeStartTap = 15000L
        val durationTap = 5000L
        val title = "Giveaway"
        coEvery { interactiveRepo.getCurrentInteractive(any()) } returns PlayCurrentInteractiveModel(
                timeStatus = PlayInteractiveTimeStatus.Scheduled(timeBeforeStartTap, durationTap),
                title = title
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
                        PlayInteractiveUiState.PreStart(timeBeforeStartTap, title)
                )
            }
        }
    }

    @Test
    fun `given has active live interactive, when retrieved, state should be ongoing`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
                WebSocketAction.NewMessage(
                        socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
                )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val interactiveRepo: PlayViewerInteractiveRepository = mockk(relaxed = true)
        val durationTap = 5000L
        val title = "Giveaway"
        coEvery { interactiveRepo.getCurrentInteractive(any()) } returns PlayCurrentInteractiveModel(
                timeStatus = PlayInteractiveTimeStatus.Live(durationTap),
                title = title
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
                        PlayInteractiveUiState.Ongoing(durationTap)
                )
            }
        }
    }

    @Test
    fun `given has active finished interactive and no leaderboard, when retrieved, there should be no interactive and no badge`() {
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
    fun `given has active finished interactive and has leaderboard, when retrieved, there should be no interactive with winner badge shown`() {
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
    fun `given has active finished interactive and leaderboard error, when retrieved, there should be no interactive and no badge`() {
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
}