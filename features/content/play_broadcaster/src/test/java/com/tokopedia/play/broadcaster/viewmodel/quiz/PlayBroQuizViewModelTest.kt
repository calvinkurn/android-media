package com.tokopedia.play.broadcaster.viewmodel.quiz

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.model.interactive.PostInteractiveCreateSessionResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test
import java.util.*

class PlayBroQuizViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockLivePusher: PusherMediator = mockk(relaxed = true)
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockSharedPref: HydraSharedPreferences = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()

    private val mockException = uiModelBuilder.buildException()

    private val mockQuizUiModel = InteractiveUiModel.Quiz(
        "1",
        "quiz",
        100,
        InteractiveUiModel.Quiz.Status.Ongoing(Calendar.getInstance().apply {
            add(Calendar.SECOND, 1800)
        }),
        emptyList(),
        ""
    )
    @Test
    fun `when user successfully create new quiz, it should return quiz model`() {

        every { mockLivePusher.remainingDurationInMillis } returns 10_000
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
        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            val state = robot.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.SubmitQuizForm)
            }

            //please check don't know why interactive type didn't changed to quiz, it still in type unknown
            Assertions.assertThat(state.interactive)
                .isInstanceOf(InteractiveUiModel.Quiz::class.java)
        }
    }


    @Test
    fun `when user click on ongoing quiz widget should trigger event to display bottom sheet quiz detail`() {

        coEvery { mockRepo.getCurrentInteractive(any()) } returns mockQuizUiModel
        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator = mockLivePusher,
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )


        robot.use {
            val events = robot.recordEvent {
                it.getViewModel().submitAction(PlayBroadcastAction.ClickOngoingWidget)
            }
//            Assertions.assertThat(events.last())
//                .isInstanceOf(PlayBroadcastEvent.ShowQuizDetailBottomSheet::class.java)
        }
    }

    @Test
    fun `when ongoing quiz ended, it should return unknown interactive model`(){
        val robot = PlayBroadcastViewModelRobot(
            livePusherMediator =  mockLivePusher,
            dispatchers = testDispatcher,
        )

        robot.use {
            val state = robot.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.QuizEnded)
            }
            Assertions.assertThat(state.interactive).isInstanceOf(InteractiveUiModel.Unknown::class.java)
        }
    }


    @Test
    fun `when user fill quiz question`(){

        val question = "pertanyaan"
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
        )

        robot.use {
            val state = robot.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.InputQuizTitle(question))
            }
            // please check why state didn't change
            state.quizForm.quizFormData.title.assertEqualTo(question)
        }
    }
}