package com.tokopedia.play.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.play.R
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.withState
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.ShowCoachMarkWinnerEvent
import com.tokopedia.play.view.uimodel.event.UiString
import com.tokopedia.play.websocket.response.PlayUserWinnerStatusSocketResponse
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketResponse
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
 * This test contains game test that starts from when we received socket that tells us
 * about the status of the game
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

    private val socket: PlayWebSocket = mockk(relaxed = true)

    private val modelBuilder = UiModelBuilder.get()

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
                interactive.game.assertEqualTo(
                    GameUiModel.Unknown
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
        val timeBeforeStartTap = 15000L.millisFromNow()
        val durationTap = 5000L.millisFromNow()
        val title = "Giveaway"

        val giveawayModel = GameUiModel.Giveaway(
            status = GameUiModel.Giveaway.Status.Upcoming(timeBeforeStartTap, durationTap),
            title = title,
            id = "1",
            waitingDuration = 200L,
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
                interactive.game.assertEqualTo(giveawayModel)
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
        val durationTap = 5000L.millisFromNow()
        val title = "Giveaway"
        val giveawayModel = GameUiModel.Giveaway(
            status = GameUiModel.Giveaway.Status.Ongoing(durationTap),
            title = title,
            id = "1",
            waitingDuration = 200L,
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
                interactive.game.assertEqualTo(giveawayModel)
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
        val giveawayModel = GameUiModel.Giveaway(
            status = GameUiModel.Giveaway.Status.Finished,
            title = title,
            id = "1",
            waitingDuration = 200L,
        )
        coEvery { repo.getCurrentInteractive(any()) } returns giveawayModel
        coEvery { repo.getInteractiveLeaderboard(any()) } returns modelBuilder.buildLeaderBoardContent(data = emptyList())

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                repo = repo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            withState {
                interactive.game.assertEqualTo(
                    GameUiModel.Unknown
                )
                winnerBadge.shouldShow.assertFalse()
            }
        }
    }

    @Test
    fun `given has interactive active quiz from socket, when retrieved, state should be ongoing`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
            )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val repo: PlayViewerRepository = mockk(relaxed = true)
        val title = "Quiz"
        val endTime = 3000L.millisFromNow()
        val model = GameUiModel.Quiz(
            status = GameUiModel.Quiz.Status.Ongoing(endTime),
            title = title,
            id = "1",
            waitingDuration = 1500L,
            listOfChoices = listOf(modelBuilder.buildQuizChoices(text = "25 June", type = PlayQuizOptionState.Default('a')))
        )
        coEvery { repo.getCurrentInteractive(any()) } returns model

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val state = it.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }
            state.interactive.game.assertEqualTo(model)
        }
    }

    @Test
    fun `given has active channel quiz from socket, when retrieved, state should be ongoing`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                socketResponseBuilder.buildQuiz()
            )
        )
        every { socket.listenAsFlow() } returns socketFlow

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val state = it.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
                socketFlow.emit(
                    WebSocketAction.NewMessage(socketResponseBuilder.buildQuiz())
                )
            }
            state.interactive.game.assertInstanceOf<GameUiModel.Quiz>()
        }
    }

    @Test
    fun `given has active quiz finished interactive, when retrieved, there should be no interactive but result badge is shown`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
            )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val repo: PlayViewerRepository = mockk(relaxed = true)
        val title = "Quiz Sepeda Ikan Koi"
        val model = GameUiModel.Quiz(
            status = GameUiModel.Quiz.Status.Finished,
            title = title,
            id = "1",
            waitingDuration = 1500L,
            listOfChoices = listOf(modelBuilder.buildQuizChoices(text = "25 June", type = PlayQuizOptionState.Default('a')))
        )

        coEvery { repo.getCurrentInteractive(any()) } returns model
        coEvery { repo.getInteractiveLeaderboard(any()) } returns modelBuilder.buildLeaderBoardContent(data = listOf(
            modelBuilder.buildWinner(name = "Koi Rainbow", imageUrl = "", topChatMessage = "", rank = 1, allowChat = { false }, id = "22")
        ))

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val state = it.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }
            state.interactive.game.assertEqualTo(
                GameUiModel.Unknown
            )
            state.winnerBadge.shouldShow.assertTrue()
        }
    }

    @Test
    fun `given has active channel quiz from socket, user has not answer`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                socketResponseBuilder.buildQuiz()
            )
        )
        every { socket.listenAsFlow() } returns socketFlow

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val state = it.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
                socketFlow.emit(
                    WebSocketAction.NewMessage(socketResponseBuilder.buildQuiz())
                )
            }
            state.interactive.game.assertInstanceOf<GameUiModel.Quiz>()
            (state.interactive.game as GameUiModel.Quiz).listOfChoices.forEach { quizChoice ->
                quizChoice.assertType<QuizChoicesUiModel> { choice ->
                    choice.type is PlayQuizOptionState.Default
                }
            }
        }
    }

    @Test
    fun `given has active channel quiz from socket, user has answered`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
            )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val repo: PlayViewerRepository = mockk(relaxed = true)
        val title = "Quiz"
        val endTime = 3000L.millisFromNow()
        val model = GameUiModel.Quiz(
            status = GameUiModel.Quiz.Status.Ongoing(endTime),
            title = title,
            id = "1",
            waitingDuration = 1500L,
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(text = "25 June", type = PlayQuizOptionState.Other(false)),
                modelBuilder.buildQuizChoices(text = "25 June", type = PlayQuizOptionState.Other(true)),
                modelBuilder.buildQuizChoices(text = "25 June", type = PlayQuizOptionState.Answered(false))
            )
        )
        coEvery { repo.getCurrentInteractive(any()) } returns model

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val state = it.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }
            (state.interactive.game as GameUiModel.Quiz).listOfChoices.forEach { quizChoice ->
                quizChoice.assertType<QuizChoicesUiModel> { choice ->
                    choice.type !is PlayQuizOptionState.Default
                }
            }
        }
    }

    @Test
    fun `given has finished channel quiz, whether user is winner or not show coachmark`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
            )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val repo: PlayViewerRepository = mockk(relaxed = true)
        val title = "Quiz"
        val model = GameUiModel.Quiz(
            status = GameUiModel.Quiz.Status.Ongoing(5000L.millisFromNow()),
            title = title,
            id = "1",
            waitingDuration = 1500L,
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(
                    text = "25 June",
                    type = PlayQuizOptionState.Other(true)
                ),
                modelBuilder.buildQuizChoices(
                    text = "25 June",
                    type = PlayQuizOptionState.Other(false)
                ),
                modelBuilder.buildQuizChoices(
                    text = "25 June",
                    type = PlayQuizOptionState.Answered(false)
                )
            )
        )
        coEvery { repo.getCurrentInteractive(any()) } returns model
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns true

        createPlayViewModelRobot(
            playChannelWebSocket = socket,
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val state = it.recordState {
                setUserId("1")
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }
            val event = it.recordEvent {
                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
                socketFlow.emit(
                    WebSocketAction.NewMessage(
                        Gson().fromJson(
                            PlayUserWinnerStatusSocketResponse.generateResponse(),
                            WebSocketResponse::class.java
                        )
                    )
                )
            }
            event.last().assertInstanceOf<ShowCoachMarkWinnerEvent>()
        }
    }
    @Test
    fun `given has finished channel quiz, has reward and user is the loser, show coachmark`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
            )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val repo: PlayViewerRepository = mockk(relaxed = true)
        val title = "Quiz"
        val model = GameUiModel.Quiz(
            status = GameUiModel.Quiz.Status.Ongoing(5000L.millisFromNow()),
            title = title,
            id = "1",
            waitingDuration = 1500L,
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(
                    text = "25 June",
                    type = PlayQuizOptionState.Other(true)
                ),
                modelBuilder.buildQuizChoices(
                    text = "25 June",
                    type = PlayQuizOptionState.Other(false)
                ),
                modelBuilder.buildQuizChoices(
                    text = "25 June",
                    type = PlayQuizOptionState.Answered(false)
                )
            )
        )
        coEvery { repo.getCurrentInteractive(any()) } returns model
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns true

        createPlayViewModelRobot(
            playChannelWebSocket = socket,
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val state = it.recordState {
                setUserId("7")
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }
            state.interactive.game.assertInstanceOf<GameUiModel.Quiz>()
            val event = it.recordEvent {
                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
                socketFlow.emit(
                    WebSocketAction.NewMessage(
                        Gson().fromJson(
                            PlayUserWinnerStatusSocketResponse.generateResponse(),
                            WebSocketResponse::class.java
                        )
                    )
                )
            }
            event.last().assertEqualTo(
                ShowCoachMarkWinnerEvent(
                    "",
                    UiString.Resource(R.string.play_quiz_finished),
                )
            )
        }
    }
    @Test
    fun `given has finished channel quiz, has no reward just show result badge`() {
        val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
            )
        )
        every { socket.listenAsFlow() } returns socketFlow

        val repo: PlayViewerRepository = mockk(relaxed = true)
        val title = "Quiz"
        val model = GameUiModel.Quiz(
            status = GameUiModel.Quiz.Status.Finished,
            title = title,
            id = "1",
            waitingDuration = 1500L,
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(
                    text = "25 June",
                    type = PlayQuizOptionState.Other(true)
                ),
                modelBuilder.buildQuizChoices(
                    text = "25 June",
                    type = PlayQuizOptionState.Other(false)
                ),
                modelBuilder.buildQuizChoices(
                    text = "25 June",
                    type = PlayQuizOptionState.Answered(false)
                )
            )
        )
        coEvery { repo.getCurrentInteractive(any()) } returns model
        coEvery { repo.getInteractiveLeaderboard(any()) } returns modelBuilder.buildLeaderBoardContent(data = listOf(
            modelBuilder.buildWinner(name = "Koi Rainbow", imageUrl = "", topChatMessage = "", rank = 1, allowChat = { false }, id = "22")
        ))

        createPlayViewModelRobot(
            playChannelWebSocket = socket,
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val state = it.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }
            it.recordEvent {
                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
            }
            state.winnerBadge.shouldShow.assertTrue()
        }
    }
}
