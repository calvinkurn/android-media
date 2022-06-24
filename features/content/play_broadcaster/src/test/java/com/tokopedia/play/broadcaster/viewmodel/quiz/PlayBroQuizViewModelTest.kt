package com.tokopedia.play.broadcaster.viewmodel.quiz

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailStateUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailStateUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

class PlayBroQuizViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()

    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123"
    )

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
    @Test
    fun `when user successfully create new quiz, it should return quiz model`() {
        coJustRun {
            mockRepo.createInteractiveQuiz(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }
        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockQuizUiModel
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getConfig()
                getViewModel().submitAction(PlayBroadcastAction.SubmitQuizForm)
            }
            Assertions.assertThat(state.interactive)
                .isInstanceOf(InteractiveUiModel.Quiz::class.java)
        }
    }

    @Test
    fun `when ongoing quiz ended, it should return unknown interactive model`(){
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val state = robot.recordState {
                getConfig()
                it.getViewModel().submitAction(PlayBroadcastAction.QuizEnded)
            }
            Assertions.assertThat(state.interactive).isInstanceOf(InteractiveUiModel.Unknown::class.java)
        }
    }

    @Test
    fun `when user fill quiz question state form data title must be same as user input`() {
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        val question = "pertanyaan"
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val state = it.recordState {
                getConfig()
                getViewModel().submitAction(PlayBroadcastAction.InputQuizTitle(question))
            }
            state.quizForm.quizFormData.title.assertEqualTo(question)
        }
    }

    @Test
    fun `when user click back on quiz choice detail participant bottom sheet should return Quiz Choice Detail Empty model`() {
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val state = it.recordState {
                getConfig()
                getViewModel().submitAction(PlayBroadcastAction.ClickBackOnChoiceDetail)
            }
            Assertions.assertThat(state.quizBottomSheetUiState.quizChoiceDetailState).isInstanceOf(
                QuizChoiceDetailStateUiModel.Empty::class.java
            )
        }
    }

    @Test
    fun `when user dismiss play bro interactive bottom sheet should return Quiz Detail Empty and Quiz Choice Detail Empty model`() {
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val state = it.recordState {
                getConfig()
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
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )
        robot.use {
            val events = it.recordEvent {
                getConfig()
                getViewModel().submitAction(PlayBroadcastAction.ClickGameResultWidget)
            }
            events.last().assertEqualTo(PlayBroadcastEvent.ShowLeaderboardBottomSheet)
        }
    }

    @Test
    fun `when user click refresh on quiz detail bottom sheet it should return quiz detail succeed state model`() {
        val mockQuizDetail = QuizDetailDataUiModel("pertanyaan", "hadiah")
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        coEvery { mockRepo.getInteractiveQuizDetail(any()) } throws mockException andThen mockQuizDetail
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )
        robot.use {
            val state = it.recordState {
                getConfig()
                getViewModel().getQuizDetailData()
                getViewModel().submitAction(PlayBroadcastAction.ClickRefreshQuizDetailBottomSheet)
            }
            Assertions.assertThat(state.quizDetail)
                .isInstanceOf(QuizDetailStateUiModel.Success::class.java)
        }
    }

    @Test
    fun `when user click refresh on leaderboard detail bottom sheet it should return quiz detail succeed state model`() {
        val mockLeaderboard = listOf(
            PlayLeaderboardUiModel(
                title = "slot 1",
                winners = emptyList(),
                id = "1",
                emptyLeaderBoardCopyText = "",
                otherParticipantText = "",
                otherParticipant = 0L
            )
        )
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
        coEvery { mockRepo.getSellerLeaderboardWithSlot(any(), any()) } throws mockException andThen mockLeaderboard
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )
        robot.use {
            val state = it.recordState {
                getConfig()
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
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
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
                getConfig()
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
        coEvery { mockRepo.getChannelConfiguration() } returns mockConfig
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
                getConfig()
                getViewModel().submitAction(
                    PlayBroadcastAction.ClickQuizChoiceOption(mockQuizChoicesUiModel)
                )
                getViewModel().submitAction(PlayBroadcastAction.ClickRefreshQuizOption)
            }
            Assertions.assertThat(state.quizBottomSheetUiState.quizChoiceDetailState)
                .isInstanceOf(QuizChoiceDetailStateUiModel.Success::class.java)
        }
    }
}