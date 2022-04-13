package com.tokopedia.play.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.model.PlayInteractiveModelBuilder
import com.tokopedia.play.model.PlaySocketResponseBuilder
import com.tokopedia.play.model.PlayVideoModelBuilder
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.withState
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

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

    private val socket: PlayWebSocket = mockk(relaxed = true)

    init {
        every { mockRemoteConfig.getBoolean(any(), any()) } returns true
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
                interactive.assertEqualTo(
                    InteractiveUiModel.Unknown
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

        val repo: PlayViewerRepository = mockk(relaxed = true)
        val timeBeforeStartTap = 15000L
        val durationTap = 5000L
        val title = "Giveaway"

        val giveawayModel = InteractiveUiModel.Giveaway(
            status = InteractiveUiModel.Giveaway.Status.Upcoming(timeBeforeStartTap, durationTap),
            title = title,
            id = 1L,
        )
        coEvery { repo.getCurrentInteractive(any()) } returns giveawayModel

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                repo = repo,
                dispatchers = testDispatcher,
                remoteConfig = mockRemoteConfig,
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactive.assertEqualTo(giveawayModel)
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

        val repo: PlayViewerRepository = mockk(relaxed = true)
        val durationTap = 5000L
        val title = "Giveaway"
        val giveawayModel = InteractiveUiModel.Giveaway(
            status = InteractiveUiModel.Giveaway.Status.Ongoing(durationTap),
            title = title,
            id = 1L,
        )
        coEvery { repo.getCurrentInteractive(any()) } returns giveawayModel

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                repo = repo,
                dispatchers = testDispatcher,
                remoteConfig = mockRemoteConfig,
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactive.assertEqualTo(giveawayModel)
            }
        }
    }

    @Test
    fun `given has active finished interactive, when retrieved, there should be no interactive`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
                WebSocketAction.NewMessage(
                        socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
                )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val repo: PlayViewerRepository = mockk(relaxed = true)
        val title = "Giveaway"
        val giveawayModel = InteractiveUiModel.Giveaway(
            status = InteractiveUiModel.Giveaway.Status.Finished,
            title = title,
            id = 1L,
        )
        coEvery { repo.getCurrentInteractive(any()) } returns giveawayModel
        coEvery { repo.getInteractiveLeaderboard(any()) } returns interactiveModelBuilder.buildLeaderboardInfo(
                leaderboardWinners = emptyList()
        )

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                repo = repo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactive.assertEqualTo(giveawayModel)
                winnerBadge.shouldShow.assertFalse()
            }
        }
    }
}