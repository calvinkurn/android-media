package com.tokopedia.play.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.interactive.AnswerQuizResponse
import com.tokopedia.play.data.repository.PlayViewerInteractiveRepositoryImpl
import com.tokopedia.play.domain.interactive.AnswerQuizUseCase
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.model.*
import com.tokopedia.play.repo.PlayViewerMockRepository
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.*
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.QuizAnsweredEvent
import com.tokopedia.play.view.uimodel.event.ShowCoachMarkWinnerEvent
import com.tokopedia.play.view.uimodel.event.ShowErrorEvent
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play_common.domain.model.interactive.GetCurrentInteractiveResponse
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 17/05/22
 */
@ExperimentalCoroutinesApi
class PlayQuizInteractiveTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val interactiveId = "12398"
    private val title = "Quiz Ikan Koi"

    private val socketResponseBuilder = PlaySocketResponseBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val videoBuilder = PlayVideoModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.Live),
        ),
        videoMetaInfo = videoBuilder.buildVideoMeta(
            videoPlayer = videoBuilder.buildCompleteGeneralVideoPlayer(),
        )
    )

    private val socketFlow = MutableStateFlow<WebSocketAction>(
        WebSocketAction.NewMessage(
            socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
        )
    )
    private val socket: PlayWebSocket = mockk(relaxed = true)
    private val mockMapper: PlayUiModelMapper = mockk(relaxed = true)
    private val modelBuilder = UiModelBuilder.get()
    private val mockInteractiveStorage = object : PlayInteractiveStorage {
        private var hasJoined = false
        private var hasProcessWinner = false
        override fun setDetail(interactiveId: String, model: PlayCurrentInteractiveModel) {}
        override fun setActive(interactiveId: String) {}
        override fun setHasProcessedWinner(interactiveId: String) {
            hasProcessWinner = true
        }
        override fun save(model: GameUiModel) {}
        override fun hasProcessedWinner(interactiveId: String): Boolean = hasProcessWinner
        override fun setJoined(id: String) {
            hasJoined = true
        }

        override fun getDetail(interactiveId: String): PlayCurrentInteractiveModel? = null
        override fun getActiveInteractiveId(): String = interactiveId
        override fun hasJoined(id: String): Boolean = hasJoined
    }

    private val mockCurrentInteractiveUseCase: GetCurrentInteractiveUseCase = mockk(relaxed = true)
    private val mockAnswerQuizUseCase: AnswerQuizUseCase = mockk(relaxed = true)

    private val duration = 3000L.millisFromNow()
    private val waitingDuration = 6000L

    private val interactiveRepo: PlayViewerInteractiveRepository =
        PlayViewerInteractiveRepositoryImpl(
            getCurrentInteractiveUseCase = mockCurrentInteractiveUseCase,
            answerQuizUseCase = mockAnswerQuizUseCase,
            getInteractiveViewerLeaderboardUseCase = mockk(relaxed = true),
            mapper = mockMapper,
            dispatchers = testDispatcher,
            interactiveStorage = mockInteractiveStorage,
            postInteractiveTapUseCase = mockk(relaxed = true)
        )

    private val mockRepo = PlayViewerMockRepository.get(interactiveRepo = interactiveRepo)

    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)

    @Before
    fun setUp(){
        every { mockRemoteConfig.getBoolean(any(), any()) } returns true
        every { socket.listenAsFlow() } returns socketFlow
        coEvery { mockCurrentInteractiveUseCase.executeOnBackground() } returns GetCurrentInteractiveResponse()
    }

    @Test
    fun `given quiz is active and answer quiz is success, when click option, then user should have joined the game`() {
        val choice = modelBuilder.buildQuizChoices(id = "3", text = "25 June", type = PlayQuizOptionState.Default('a'))

        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Quiz(
            id = interactiveId,
            title = title,
            waitingDuration = waitingDuration,
            status = GameUiModel.Quiz.Status.Ongoing(
                endTime = duration,
            ),
            listOfChoices = listOf(choice)
        )

        coEvery { mockAnswerQuizUseCase.executeOnBackground() } returns AnswerQuizResponse(data = AnswerQuizResponse.Data(correctAnswerID = "3"))

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)
            it.setLoggedIn(true)
            it.setUserId("1")

            mockInteractiveStorage.hasJoined(interactiveId).assertFalse()
            it.recordEvent {
                it.viewModel.submitAction(
                    PlayViewerNewAction.ClickQuizOptionAction(item = choice)
                )
            }.last().assertEqualTo(QuizAnsweredEvent(isTrue = true))
            mockInteractiveStorage.hasJoined(interactiveId).assertTrue()
        }
    }

    @Test
    fun `given quiz is active and answer quiz is error, when click option, then user should not have joined the game`() {
        val choice = modelBuilder.buildQuizChoices(id = "3", text = "25 June", type = PlayQuizOptionState.Default('a'))

        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Quiz(
            id = interactiveId,
            title = title,
            waitingDuration = waitingDuration,
            status = GameUiModel.Quiz.Status.Ongoing(
                endTime = duration,
            ),
            listOfChoices = listOf(choice)
        )
        val err = MessageErrorException("Error gk bs jawab y")

        coEvery { mockRepo.answerQuiz(any(), any()) } throws err

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)
            it.setLoggedIn(true)
            it.setUserId("1")

            mockInteractiveStorage.hasJoined(interactiveId).assertFalse()
            it.recordEvent {
                it.viewModel.submitAction(
                    PlayViewerNewAction.ClickQuizOptionAction(item = choice)
                )
            }.last().assertEqualTo(ShowErrorEvent(err))
            mockInteractiveStorage.hasJoined(interactiveId).assertFalse()
        }
    }

    @Test
    fun `given quiz is active and user has not answer question, then click answer, quiz options state needs to change`() {
        val selectedId = "3"
        val selectedChoice = modelBuilder.buildQuizChoices(id = selectedId, text = "25 June", type = PlayQuizOptionState.Default('a'))

        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Quiz(
            id = interactiveId,
            title = title,
            waitingDuration = waitingDuration,
            status = GameUiModel.Quiz.Status.Ongoing(
                endTime = duration,
            ),
            listOfChoices = listOf(
                selectedChoice,
                modelBuilder.buildQuizChoices(id = "31", text = "25 Juky", type = PlayQuizOptionState.Default('b')),
                modelBuilder.buildQuizChoices(id = "32", text = "25 Juky", type = PlayQuizOptionState.Default('b')),
            )
        )

        coEvery { mockAnswerQuizUseCase.executeOnBackground() } returns AnswerQuizResponse(data = AnswerQuizResponse.Data(correctAnswerID = "31"))

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val eventAndState = it.recordStateAndEvent {
                createPage(mockChannelData)
                focusPage(mockChannelData)
                setLoggedIn(true)
                setUserId("1")

                mockInteractiveStorage.hasJoined(interactiveId).assertFalse()
                viewModel.submitAction(PlayViewerNewAction.ClickQuizOptionAction(item = selectedChoice))
                mockInteractiveStorage.hasJoined(interactiveId).assertTrue()
            }
            eventAndState.second.last().assertEqualTo(QuizAnsweredEvent(isTrue = false))
            (eventAndState.first.interactive.game as GameUiModel.Quiz).listOfChoices.forEach { quizChoice ->
                quizChoice.assertType<QuizChoicesUiModel> { choice ->
                    if(choice.id == selectedId) choice.type is PlayQuizOptionState.Answered
                }
            }
        }
    }

    @Test
    fun `given quiz is active and user has already answered question, user can't click any options - not in default state`() {
        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Quiz(
            id = interactiveId,
            title = title,
            waitingDuration = waitingDuration,
            status = GameUiModel.Quiz.Status.Ongoing(
                endTime = duration,
            ),
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(id = "3", text = "25 June", type = PlayQuizOptionState.Answered(false)),
                modelBuilder.buildQuizChoices(id = "31", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
                modelBuilder.buildQuizChoices(id = "32", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
            )
        )

        coEvery { mockAnswerQuizUseCase.executeOnBackground() } returns AnswerQuizResponse(data = AnswerQuizResponse.Data(correctAnswerID = "31"))

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val eventAndState = it.recordStateAndEvent {
                createPage(mockChannelData)
                focusPage(mockChannelData)
                setLoggedIn(true)
                setUserId("1")
            }
            (eventAndState.first.interactive.game as GameUiModel.Quiz).listOfChoices.forEach { quizChoice ->
                quizChoice.assertType<QuizChoicesUiModel> { choice ->
                    choice.type !is PlayQuizOptionState.Default
                }
            }
        }
    }

    @Test
    fun `given quiz is finish and has no reward, only show leaderboard`() {
        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Quiz(
            id = interactiveId,
            title = title,
            waitingDuration = waitingDuration,
            status = GameUiModel.Quiz.Status.Ongoing(
                endTime = duration,
            ),
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(id = "3", text = "25 June", type = PlayQuizOptionState.Answered(false)),
                modelBuilder.buildQuizChoices(id = "31", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
                modelBuilder.buildQuizChoices(id = "32", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
            )
        )

        coEvery { mockAnswerQuizUseCase.executeOnBackground() } returns AnswerQuizResponse(data = AnswerQuizResponse.Data(correctAnswerID = "31"))

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val eventAndState = it.recordStateAndEvent {
                createPage(mockChannelData)
                focusPage(mockChannelData)
                setLoggedIn(true)
                setUserId("1")

                mockInteractiveStorage.hasProcessedWinner(interactiveId).assertFalse()

                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
            }
            eventAndState.first.winnerBadge.shouldShow.assertTrue()
            mockInteractiveStorage.hasProcessedWinner(interactiveId).assertTrue()
        }
    }

    @Test
    fun `given quiz is finish and not received any socket userwinner, only show leaderboard`() {
        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Quiz(
            id = interactiveId,
            title = title,
            waitingDuration = 0L,
            status = GameUiModel.Quiz.Status.Ongoing(
                endTime = duration,
            ),
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(id = "3", text = "25 June", type = PlayQuizOptionState.Answered(false)),
                modelBuilder.buildQuizChoices(id = "31", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
                modelBuilder.buildQuizChoices(id = "32", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
            )
        )

        coEvery { mockAnswerQuizUseCase.executeOnBackground() } returns AnswerQuizResponse(data = AnswerQuizResponse.Data(correctAnswerID = "31"))

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val eventAndState = it.recordStateAndEvent {
                createPage(mockChannelData)
                focusPage(mockChannelData)
                setLoggedIn(true)
                setUserId("1")

                mockInteractiveStorage.hasProcessedWinner(interactiveId).assertFalse()

                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
            }
            mockInteractiveStorage.hasProcessedWinner(interactiveId).assertTrue()
            eventAndState.first.winnerBadge.shouldShow.assertTrue()
            eventAndState.first.interactive.game.assertInstanceOf<GameUiModel.Unknown>()
        }
    }

    @Test
    fun `user participate - given quiz is finish and show coachmark and leaderboard`() {
        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Quiz(
            id = interactiveId,
            title = title,
            waitingDuration = waitingDuration,
            status = GameUiModel.Quiz.Status.Ongoing(
                endTime = duration,
            ),
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(id = "31", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
                modelBuilder.buildQuizChoices(id = "32", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
            )
        )

        coEvery { mockAnswerQuizUseCase.executeOnBackground() } returns AnswerQuizResponse(data = AnswerQuizResponse.Data(correctAnswerID = "31"))

        val selectedChoice = modelBuilder.buildQuizChoices(id = "31", text = "25 June", type = PlayQuizOptionState.Default('a'))

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val eventAndState = it.recordStateAndEvent {
                createPage(mockChannelData)
                focusPage(mockChannelData)
                setLoggedIn(true)
                setUserId("1")

                viewModel.submitAction(PlayViewerNewAction.ClickQuizOptionAction(item = selectedChoice))

                mockInteractiveStorage.hasProcessedWinner(interactiveId).assertFalse()

                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
            }
            mockInteractiveStorage.hasJoined(interactiveId).assertTrue()
            mockInteractiveStorage.hasProcessedWinner(interactiveId).assertTrue()
            eventAndState.first.winnerBadge.shouldShow.assertTrue()
            eventAndState.second.last().assertInstanceOf<ShowCoachMarkWinnerEvent>()
        }
    }

    @Test
    fun `user not participate - given quiz is finish and show coachmark and leaderboard`() {
        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Quiz(
            id = interactiveId,
            title = title,
            waitingDuration = waitingDuration,
            status = GameUiModel.Quiz.Status.Ongoing(
                endTime = duration,
            ),
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(id = "31", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
                modelBuilder.buildQuizChoices(id = "32", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
            )
        )

        coEvery { mockAnswerQuizUseCase.executeOnBackground() } returns AnswerQuizResponse(data = AnswerQuizResponse.Data(correctAnswerID = "31"))

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val eventAndState = it.recordStateAndEvent {
                createPage(mockChannelData)
                focusPage(mockChannelData)
                setLoggedIn(true)
                setUserId("1")

                mockInteractiveStorage.hasProcessedWinner(interactiveId).assertFalse()

                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
            }
            mockInteractiveStorage.hasJoined(interactiveId).assertFalse()
            mockInteractiveStorage.hasProcessedWinner(interactiveId).assertTrue()
            eventAndState.first.winnerBadge.shouldShow.assertTrue()
            eventAndState.second.last().assertInstanceOf<ShowCoachMarkWinnerEvent>()
        }
    }

    @Test
    fun `non login user - given quiz is finish and show coachmark and leaderboard`() {
        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Quiz(
            id = interactiveId,
            title = title,
            waitingDuration = waitingDuration,
            status = GameUiModel.Quiz.Status.Ongoing(
                endTime = duration,
            ),
            listOfChoices = listOf(
                modelBuilder.buildQuizChoices(id = "31", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
                modelBuilder.buildQuizChoices(id = "32", text = "25 Juky", type = PlayQuizOptionState.Other(true)),
            )
        )

        coEvery { mockAnswerQuizUseCase.executeOnBackground() } returns AnswerQuizResponse(data = AnswerQuizResponse.Data(correctAnswerID = "31"))

        createPlayViewModelRobot (
            playChannelWebSocket = socket,
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            val eventAndState = it.recordStateAndEvent {
                createPage(mockChannelData)
                focusPage(mockChannelData)
                setLoggedIn(false)

                mockInteractiveStorage.hasProcessedWinner(interactiveId).assertFalse()

                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
            }
            mockInteractiveStorage.hasJoined(interactiveId).assertFalse()
            mockInteractiveStorage.hasProcessedWinner(interactiveId).assertTrue()
            eventAndState.first.winnerBadge.shouldShow.assertTrue()
            eventAndState.second.last().assertInstanceOf<ShowCoachMarkWinnerEvent>()
        }
    }
}
