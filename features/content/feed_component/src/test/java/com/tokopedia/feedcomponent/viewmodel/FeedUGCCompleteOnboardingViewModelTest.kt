package com.tokopedia.feedcomponent.viewmodel

import com.tokopedia.feedcomponent.model.CommonModelBuilder
import com.tokopedia.feedcomponent.onboarding.domain.repository.FeedUGCOnboardingRepository
import com.tokopedia.feedcomponent.onboarding.view.strategy.FeedUGCCompleteOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.UsernameState
import com.tokopedia.feedcomponent.robot.FeedUGCOnboardingViewModelRobot
import com.tokopedia.feedcomponent.util.*
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FeedUGCCompleteOnboardingViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: FeedUGCOnboardingRepository = mockk(relaxed = true)

    private val commonBuilder = CommonModelBuilder()

    private val mockException = commonBuilder.buildException()
    private val mockValidUsername = "jonathandarwin"
    private val mockInvalidUsername = "aa"
    private val mockErrorUsernameValidation = "Username minimal 3 karakter"

    private val completeStrategy = FeedUGCCompleteOnboardingStrategy(
        dispatcher = testDispatcher,
        repo = mockRepo,
    )

    private val robot = FeedUGCOnboardingViewModelRobot(
        onboardingStrategy = completeStrategy,
        dispatcher = testDispatcher,
    )

    @Before
    fun setUp() {
        coEvery { mockRepo.validateUsername(mockValidUsername) } returns Pair(true, "")
        coEvery { mockRepo.validateUsername(mockInvalidUsername) } returns Pair(false, mockErrorUsernameValidation)
        coEvery { mockRepo.insertUsername(any()) } returns true
        coEvery { mockRepo.acceptTnc() } returns true
    }

    @Test
    fun `when user input valid username, it should emit username valid state`() {

        robot.use {
            it.recordState {
                submitAction(FeedUGCOnboardingAction.InputUsername(mockValidUsername))
            } andThen {
                usernameState equalTo UsernameState.Valid
                username equalTo mockValidUsername
            }
        }
    }

    @Test
    fun `when user input invalid username, it should emit username error state`() {

        robot.use {
            it.recordState {
                submitAction(FeedUGCOnboardingAction.InputUsername(mockInvalidUsername))
            } andThen {
                usernameState equalTo UsernameState.Invalid(mockErrorUsernameValidation)
                username equalTo mockInvalidUsername
            }
        }
    }

    @Test
    fun `when user input empty username, it should emit username unknown state`() {

        robot.use {
            it.recordState {
                submitAction(FeedUGCOnboardingAction.InputUsername(""))
            } andThen {
                usernameState equalTo UsernameState.Unknown
                username equalTo ""
            }
        }
    }

    @Test
    fun `when user wants to validate username but error happen on mobile, it should emit username invalid state`() {

        coEvery { mockRepo.validateUsername(any()) } throws mockException

        robot.use {
            it.recordState {
                submitAction(FeedUGCOnboardingAction.InputUsername(mockValidUsername))
            } andThen {
                usernameState equalTo UsernameState.Invalid("")
                username equalTo mockValidUsername
            }
        }
    }

    @Test
    fun `when user click unchecked checkbox, it should check the checkbox`() {
        robot.use {
            it.recordState {
                submitAction(FeedUGCOnboardingAction.CheckTnc)
            } andThen {
                isCheckTnc.assertTrue()
            }
        }
    }

    @Test
    fun `when user click checked checkbox, it should uncheck the checkbox`() {
        robot.use {
            it.recordState {
                submitAction(FeedUGCOnboardingAction.CheckTnc)
                submitAction(FeedUGCOnboardingAction.CheckTnc)
            } andThen {
                isCheckTnc.assertFalse()
            }
        }
    }

    @Test
    fun `when user wants to submit tnc but havent check tnc, it should not do anything`() {
        robot.use {
            it.recordState {
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen {
                isSubmit.assertFalse()
                hasAcceptTnc.assertFalse()
            }
        }
    }

    @Test
    fun `when user wants to submit tnc but havent validate username, it should not do anything`() {

        robot.use {
            it.recordState {
                submitAction(FeedUGCOnboardingAction.InputUsername(mockInvalidUsername))
                submitAction(FeedUGCOnboardingAction.CheckTnc)
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen {
                isSubmit.assertFalse()
                hasAcceptTnc.assertFalse()
            }
        }
    }

    @Test
    fun `when user wants to submit tnc but suddenly username become not valid, it should emit invalid username`() {

        val mockErrorUsernameExists = "Username sudah digunakan"

        robot.use {
            it.setup {
                submitAction(FeedUGCOnboardingAction.InputUsername(mockValidUsername))
                submitAction(FeedUGCOnboardingAction.CheckTnc)

                coEvery { mockRepo.validateUsername(any()) } returns Pair(false, mockErrorUsernameExists)
            } recordState {
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen {
                usernameState equalTo UsernameState.Invalid(mockErrorUsernameExists)
                isSubmit.assertFalse()
                hasAcceptTnc.assertFalse()
            }
        }
    }

    @Test
    fun `when user wants to submit tnc but error when submit username, it should emit error event`() {

        coEvery { mockRepo.insertUsername(any()) } returns false

        robot.use {
            it.setup {
                submitAction(FeedUGCOnboardingAction.InputUsername(mockValidUsername))
                submitAction(FeedUGCOnboardingAction.CheckTnc)
            } recordStateAndEvent  {
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen { state, events ->
                events.last().assertEvent(FeedUGCOnboardingUiEvent.ShowError)

                state.isSubmit.assertFalse()
                state.hasAcceptTnc.assertFalse()
            }
        }
    }

    @Test
    fun `when user wants to submit tnc but error when submit tnc, it should emit error event`() {

        coEvery { mockRepo.acceptTnc() } returns false

        robot.use {
            it.setup {
                submitAction(FeedUGCOnboardingAction.InputUsername(mockValidUsername))
                submitAction(FeedUGCOnboardingAction.CheckTnc)
            } recordStateAndEvent  {
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen { state, events ->
                events.last().assertEvent(FeedUGCOnboardingUiEvent.ShowError)

                state.isSubmit.assertFalse()
                state.hasAcceptTnc.assertFalse()
            }
        }
    }

    @Test
    fun `when user wants to submit tnc but have no internet connection, it should emit error event`() {

        coEvery { mockRepo.acceptTnc() } throws mockException

        robot.use {
            it.setup {
                submitAction(FeedUGCOnboardingAction.InputUsername(mockValidUsername))
                submitAction(FeedUGCOnboardingAction.CheckTnc)
            } recordStateAndEvent  {
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen { state, events ->
                events.last().assertEvent(FeedUGCOnboardingUiEvent.ShowError)

                state.isSubmit.assertFalse()
                state.hasAcceptTnc.assertFalse()
            }
        }
    }

    @Test
    fun `when user successfully to submit tnc, it should emit success state`() {

        robot.use {
            it.setup {
                submitAction(FeedUGCOnboardingAction.InputUsername(mockValidUsername))
                submitAction(FeedUGCOnboardingAction.CheckTnc)
            } recordState {
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen {
                isSubmit.assertFalse()
                hasAcceptTnc.assertTrue()
            }
        }
    }
}