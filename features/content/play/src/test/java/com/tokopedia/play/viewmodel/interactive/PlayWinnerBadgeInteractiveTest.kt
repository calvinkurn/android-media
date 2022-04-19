package com.tokopedia.play.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play.extensions.isLeaderboardSheetShown
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.withState
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.action.ClickCloseLeaderboardSheetAction
import com.tokopedia.play.view.uimodel.action.InteractiveWinnerBadgeClickedAction
import com.tokopedia.play.view.uimodel.action.RefreshLeaderboard
import com.tokopedia.play.view.uimodel.state.PlayInteractiveUiState
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play_common.model.ui.PlayLeaderboardWrapperUiModel
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.*
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 15/07/21
 */
class PlayWinnerBadgeInteractiveTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val socketResponseBuilder = PlaySocketResponseBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val videoInfoBuilder = PlayVideoModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData(
            channelDetail = channelInfoBuilder.buildChannelDetail(
                    channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.Live),
            ),
            videoMetaInfo = videoInfoBuilder.buildVideoMeta(
                    videoPlayer = videoInfoBuilder.buildCompleteGeneralVideoPlayer()
            )
    )

    private val mockRemoteConfig: RemoteConfig = mockk(relaxed = true)

    private val interactiveModelBuilder = PlayInteractiveModelBuilder()

    private val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                    socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
            )
    )
    private val socket: PlayWebSocket = mockk(relaxed = true)

    private val interactiveRepo: PlayViewerRepository = mockk(relaxed = true)

    init {
        every { socket.listenAsFlow() } returns socketFlow
        every { mockRemoteConfig.getBoolean(any(), any()) } returns true

        every { interactiveRepo.getChannelData(any()) } returns mockChannelData
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
                repo = interactiveRepo,
                dispatchers = testDispatcher,
                remoteConfig = mockRemoteConfig,
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactiveView.interactive.isEqualTo(
                        PlayInteractiveUiState.NoInteractive
                )
                winnerBadge.shouldShow.assertTrue()
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
                repo = interactiveRepo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactiveView.interactive.isEqualTo(
                        PlayInteractiveUiState.NoInteractive
                )
                winnerBadge.shouldShow.assertFalse()
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

        val interactiveRepo: PlayViewerRepository = mockk(relaxed = true)
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
                repo = interactiveRepo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactiveView.interactive.isEqualTo(
                        PlayInteractiveUiState.NoInteractive
                )
                winnerBadge.shouldShow.assertFalse()
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
                repo = interactiveRepo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.andWhen {
            viewModel.submitAction(InteractiveWinnerBadgeClickedAction(10))
        }.thenVerify {
            withState {
                bottomInsets.isLeaderboardSheetShown.assertTrue()
            }
        }.andWhen {
            viewModel.submitAction(ClickCloseLeaderboardSheetAction)
        }.thenVerify {
            withState {
                bottomInsets.isLeaderboardSheetShown.assertFalse()
            }
        }
    }

    @Test
    fun `given refresh leaderboard, if should query true, then viewmodel will first emit loading state`() {
        coEvery { interactiveRepo.getInteractiveLeaderboard(any()) } returns interactiveModelBuilder.buildLeaderboardInfo(
            leaderboardWinners = listOf(interactiveModelBuilder.buildLeaderboard())
        )

        givenPlayViewModelRobot(
            playChannelWebSocket = socket,
            repo = interactiveRepo,
            dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.andWhen {
            viewModel.submitAction(RefreshLeaderboard)
        }.thenVerify {
            withState {
                winnerBadge.leaderboards.isEqualTo(PlayLeaderboardWrapperUiModel.Loading)
            }
        }
    }
}