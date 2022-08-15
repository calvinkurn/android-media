package com.tokopedia.feedcomponent.viewmodel

import com.tokopedia.feedcomponent.model.CommonModelBuilder
import com.tokopedia.feedcomponent.onboarding.domain.repository.FeedUGCOnboardingRepository
import com.tokopedia.feedcomponent.onboarding.view.strategy.FeedUGCTncOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.robot.FeedUGCOnboardingViewModelRobot
import com.tokopedia.feedcomponent.util.andThen
import com.tokopedia.feedcomponent.util.assertEvent
import com.tokopedia.feedcomponent.util.assertFalse
import com.tokopedia.feedcomponent.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FeedUGCTncOnboardingViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: FeedUGCOnboardingRepository = mockk(relaxed = true)

    private val commonBuilder = CommonModelBuilder()

    private val mockException = commonBuilder.buildException()

    private val tncStrategy = FeedUGCTncOnboardingStrategy(
        dispatcher = testDispatcher,
        repo = mockRepo,
    )

    private val robot = FeedUGCOnboardingViewModelRobot(
        onboardingStrategy = tncStrategy,
        dispatcher = testDispatcher,
    )

    @Before
    fun setUp() {
        coEvery { mockRepo.acceptTnc() } returns true
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
    fun `when user successfully to submit tnc , it should emit success state`() {
        robot.use {
            it.recordState {
                submitAction(FeedUGCOnboardingAction.CheckTnc)
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen {
                isSubmit.assertFalse()
                hasAcceptTnc.assertTrue()
            }
        }
    }

    @Test
    fun `when user submit tnc and error happen on BE, it should emit error state`() {

        coEvery { mockRepo.acceptTnc() } returns false

        robot.use {
            it.recordStateAndEvent {
                submitAction(FeedUGCOnboardingAction.CheckTnc)
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen { state, events ->
                events.last().assertEvent(FeedUGCOnboardingUiEvent.ShowError)

                state.isSubmit.assertFalse()
                state.hasAcceptTnc.assertFalse()
            }
        }
    }

    @Test
    fun `when user submit tnc and error happen on mobile, it should emit error state`() {

        coEvery { mockRepo.acceptTnc() } throws mockException

        robot.use {
            it.recordStateAndEvent {
                submitAction(FeedUGCOnboardingAction.CheckTnc)
                submitAction(FeedUGCOnboardingAction.ClickNext)
            } andThen { state, events ->
                events.last().assertEvent(FeedUGCOnboardingUiEvent.ShowError)

                state.isSubmit.assertFalse()
                state.hasAcceptTnc.assertFalse()
            }
        }
    }
}