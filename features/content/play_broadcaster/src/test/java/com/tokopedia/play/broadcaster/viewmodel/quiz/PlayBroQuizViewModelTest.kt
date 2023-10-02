package com.tokopedia.play.broadcaster.viewmodel.quiz

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.interactive.InteractiveUiModelBuilder
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.model.game.quiz.*
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlayBroQuizViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    private val mockSharedPref: HydraSharedPreferences = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()

    private val interactiveUiModelBuilder = InteractiveUiModelBuilder()

    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123"
    )

    private val mockInteractiveConfigResponse = interactiveUiModelBuilder.buildInteractiveConfigModel()

    private val mockException = uiModelBuilder.buildException()

    private val mockQuizUiModel = uiModelBuilder.buildQuizModel(
        id = "1",
        title = "quiz",
        waitingDuration = 15,
        duration = 180,
        choices = listOf(
            QuizChoicesUiModel(
                0,
                "1",
                "AAAA",
                PlayQuizOptionState.Participant('A', true, "0", false)
            ),
            QuizChoicesUiModel(
                1,
                "2",
                "BBBB",
                PlayQuizOptionState.Participant('B', false, "0", false)
            )
        )
    )

    private val mockBroadcastTimer: PlayBroadcastTimer = mockk(relaxed = true)

    @Before
    fun setUp() {
        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns uiModelBuilder.buildBroadcastingConfigUiModel()
    }

    @Test
    fun `when user successfully create new quiz, it should return quiz model`() {
        coJustRun {
            mockRepo.createInteractiveQuiz(
                any(),
                any(),
                any(),
                any(),
            )
        }
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockQuizUiModel
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.SubmitQuizForm)
            }
            Assertions.assertThat(state.game)
                .isInstanceOf(GameUiModel.Quiz::class.java)
        }
    }

    @Test
    fun `when ongoing quiz ended, it should return unknown interactive model`(){
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                it.getViewModel().submitAction(PlayBroadcastAction.QuizEnded)
            }
            Assertions.assertThat(state.game).isInstanceOf(GameUiModel.Unknown::class.java)
        }
    }

    @Test
    fun `when user fill quiz question state form data title must be same as user input`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        val question = "pertanyaan"
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.InputQuizTitle(question))
            }
            state.quizForm.quizFormData.title.assertEqualTo(question)
        }
    }

    @Test
    fun `when user click back on quiz choice detail participant bottom sheet should return Quiz Choice Detail Empty model`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.ClickBackOnChoiceDetail)
            }
            Assertions.assertThat(state.quizBottomSheetUiState.quizChoiceDetailState).isInstanceOf(
                QuizChoiceDetailStateUiModel.Empty::class.java
            )
        }
    }

    @Test
    fun `when user dismiss play bro interactive bottom sheet should return Quiz Detail Empty and Quiz Choice Detail Empty model`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.DismissQuizDetailBottomSheet)
            }
            Assertions.assertThat(state.quizBottomSheetUiState.quizChoiceDetailState).isInstanceOf(
                QuizChoiceDetailStateUiModel.Empty::class.java
            )
            Assertions.assertThat(state.quizBottomSheetUiState.quizDetailState).isInstanceOf(
                QuizDetailStateUiModel.Empty::class.java
            )
        }
    }

    @Test
    fun `when user click on game result widget should emit event show Leaderboard BottomSheet`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val events = it.recordEvent {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.ClickGameResultWidget)
            }
            events.last().assertEqualTo(PlayBroadcastEvent.ShowLeaderboardBottomSheet)
        }
    }

    @Test
    fun `when user click refresh on quiz detail bottom sheet it should return quiz detail succeed state model`() {
        val mockQuizDetail = QuizDetailDataUiModel("pertanyaan", "hadiah")
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getInteractiveQuizDetail(any()) } throws mockException andThen mockQuizDetail
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )
        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().getQuizDetailData()
                getViewModel().submitAction(PlayBroadcastAction.ClickRefreshQuizDetailBottomSheet)
            }
            Assertions.assertThat(state.quizDetail)
                .isInstanceOf(QuizDetailStateUiModel.Success::class.java)
        }
    }

    @Test
    fun `when user click refresh on leaderboard detail bottom sheet it should return quiz detail succeed state model`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } throws mockException andThen interactiveUiModelBuilder.buildLeaderboardInfoModel(
            listOf(LeaderboardGameUiModel.Header(leaderBoardType = LeadeboardType.Quiz, id = "11", title = "Hehe")))
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )
        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().getLeaderboardWithSlots()
                getViewModel().submitAction(PlayBroadcastAction.ClickRefreshQuizDetailBottomSheet)
            }
            Assertions.assertThat(state.quizDetail)
                .isInstanceOf(QuizDetailStateUiModel.Success::class.java)
        }
    }

    @Test
    fun `when user click on option detail in bottom sheet it should return quiz choice option detail with participant`() {
        val mockQuizChoicesUiModel = QuizChoicesUiModel(
            id = "1",
            index = 0,
            type = PlayQuizOptionState.Participant('A', false, "100", false),
            text = "jawaban a"
        )
        val mockQuizChoiceDetailUiModel = QuizChoiceDetailUiModel(
            choice = mockQuizChoicesUiModel,
            cursor = "-1"
        )
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery {
            mockRepo.getInteractiveQuizChoiceDetail(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockQuizChoiceDetailUiModel
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(
                    PlayBroadcastAction.ClickQuizChoiceOption(
                        mockQuizChoicesUiModel
                    )
                )
            }
            Assertions.assertThat(state.quizBottomSheetUiState.quizChoiceDetailState)
                .isInstanceOf(QuizChoiceDetailStateUiModel.Success::class.java)
        }
    }

    @Test
    fun `when user click refresh quiz choice option detail it should return quiz choice detail state success ui model`() {
        val mockQuizChoicesUiModel = QuizChoicesUiModel(
            id = "1",
            index = 0,
            type = PlayQuizOptionState.Participant('A', false, "100", false),
            text = "jawaban a"
        )
        val mockQuizChoiceDetailUiModel = QuizChoiceDetailUiModel(
            choice = mockQuizChoicesUiModel,
            cursor = "-1"
        )
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery {
            mockRepo.getInteractiveQuizChoiceDetail(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws mockException andThen mockQuizChoiceDetailUiModel
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(
                    PlayBroadcastAction.ClickQuizChoiceOption(mockQuizChoicesUiModel)
                )
                getViewModel().submitAction(PlayBroadcastAction.ClickRefreshQuizOption)
            }
            Assertions.assertThat(state.quizBottomSheetUiState.quizChoiceDetailState)
                .isInstanceOf(QuizChoiceDetailStateUiModel.Success::class.java)
        }
    }

    @Test
    fun `when user click quiz game option should change quiz form state to preparation`(){
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.ClickGameOption(GameType.Quiz))
            }
            Assertions.assertThat(state.quizForm.quizFormState).isInstanceOf(QuizFormStateUiModel.Preparation::class.java)
        }
    }

    @Test
    fun `when user click giveaway game option should change interactive setup type to giveaway type`(){
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.ClickGameOption(GameType.Giveaway))
            }
            Assertions.assertThat(state.interactiveSetup.type).isEqualTo(GameType.Giveaway)
        }
    }

    @Test
    fun `when user click unknown game option should change interactive setup type to unknown type`(){
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.ClickGameOption(GameType.Unknown))
            }
            Assertions.assertThat(state.interactiveSetup.type).isEqualTo(GameType.Unknown)
        }
    }

    @Test
    fun `when user fill input option data, quiz form state form data options must filled with input option value`(){
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns emptyList()

        every { mockBroadcastTimer.remainingDuration } returns Long.MAX_VALUE

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref =  mockSharedPref,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                startLive()
                inputQuizOption(0, "AAA")
            }
         state.quizForm.quizFormData.options[0].text.assertEqualTo("AAA")
        }
    }

    @Test
    fun `when user click back on quiz`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns emptyList()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref =  mockSharedPref,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                startLive()
                it.getViewModel().submitAction(PlayBroadcastAction.ClickBackOnQuiz)
            }

            state.quizForm.quizFormState.assertEqualTo(QuizFormStateUiModel.Nothing)
        }
    }

    @Test
    fun `when user click next on quiz from nothing to preparation`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns emptyList()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref =  mockSharedPref,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                startLive()
                it.getViewModel().submitAction(PlayBroadcastAction.ClickNextOnQuiz)
            }

            state.quizForm.quizFormState.assertEqualTo(QuizFormStateUiModel.Preparation)
        }
    }

    @Test
    fun `when user click next on quiz from preparation to set duration`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns emptyList()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref =  mockSharedPref,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                startLive()
                it.getViewModel().submitAction(PlayBroadcastAction.ClickNextOnQuiz)
                it.getViewModel().submitAction(PlayBroadcastAction.ClickNextOnQuiz)
            }

            state.quizForm.quizFormState.assertEqualTo(QuizFormStateUiModel.SetDuration(isLoading = false))
        }
    }

    @Test
    fun `when user select quiz option`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns emptyList()

        every { mockBroadcastTimer.remainingDuration } returns Long.MAX_VALUE

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref =  mockSharedPref,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                startLive()
                inputQuizOption(0, "AAA")
                inputQuizOption(1, "BBB")
                it.getViewModel().submitAction(PlayBroadcastAction.SelectQuizOption(0))
            }

            state.quizForm.quizFormData.options[0].isSelected.assertTrue()
        }
    }

    @Test
    fun `when user select quiz option and not valid`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns emptyList()

        every { mockBroadcastTimer.remainingDuration } returns Long.MAX_VALUE

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref =  mockSharedPref,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                startLive()
                inputQuizOption(0, "AAA")
                inputQuizOption(1, "BBB")
                it.getViewModel().submitAction(PlayBroadcastAction.SelectQuizOption(-1))
            }

            state.quizForm.quizFormData.options[0].isSelected.assertFalse()
        }
    }

    @Test
    fun `when user save quiz data`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns emptyList()

        every { mockBroadcastTimer.remainingDuration } returns Long.MAX_VALUE

        val mockData = QuizFormDataUiModel("aBcD")

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref =  mockSharedPref,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                startLive()
                it.getViewModel().submitAction(PlayBroadcastAction.SaveQuizData(mockData))
            }

            state.quizForm.quizFormData.assertEqualTo(mockData)
        }
    }

    @Test
    fun `when user select quiz duration`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns mockInteractiveConfigResponse
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } returns emptyList()

        every { mockBroadcastTimer.remainingDuration } returns Long.MAX_VALUE

        val mockData = 1000L

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref =  mockSharedPref,
            broadcastTimer = mockBroadcastTimer,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                startLive()
                it.getViewModel().submitAction(PlayBroadcastAction.SelectQuizDuration(mockData))
            }

            state.quizForm.quizFormData.durationInMs.assertEqualTo(mockData)
        }
    }

}
