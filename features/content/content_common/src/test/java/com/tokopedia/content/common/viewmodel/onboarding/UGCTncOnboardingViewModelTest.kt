package com.tokopedia.content.common.viewmodel.onboarding

import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.onboarding.view.strategy.UGCTncOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.uimodel.action.UGCOnboardingAction
import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.robot.UGCOnboardingViewModelRobot
import com.tokopedia.content.common.model.CommonModelBuilder
import com.tokopedia.content.common.util.andThen
import com.tokopedia.content.common.util.assertEvent
import com.tokopedia.content.common.util.assertFalse
import com.tokopedia.content.common.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class UGCTncOnboardingViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: UGCOnboardingRepository = mockk(relaxed = true)

    private val commonBuilder = CommonModelBuilder()

    private val mockException = commonBuilder.buildException()

    private val tncStrategy = UGCTncOnboardingStrategy(
        dispatcher = testDispatcher,
        repo = mockRepo,
    )

    private val robot = UGCOnboardingViewModelRobot(
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
                submitAction(UGCOnboardingAction.CheckTnc)
            } andThen {
                isCheckTnc.assertTrue()
            }
        }
    }

    @Test
    fun `when user click checked checkbox, it should uncheck the checkbox`() {
        robot.use {
            it.recordState {
                submitAction(UGCOnboardingAction.CheckTnc)
                submitAction(UGCOnboardingAction.CheckTnc)
            } andThen {
                isCheckTnc.assertFalse()
            }
        }
    }

    @Test
    fun `when user wants to submit tnc but havent check tnc, it should not do anything`() {
        robot.use {
            it.recordState {
                submitAction(UGCOnboardingAction.ClickNext)
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
                submitAction(UGCOnboardingAction.CheckTnc)
                submitAction(UGCOnboardingAction.ClickNext)
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
                submitAction(UGCOnboardingAction.CheckTnc)
                submitAction(UGCOnboardingAction.ClickNext)
            } andThen { state, events ->
                events.last().assertEvent(UGCOnboardingUiEvent.ShowError)

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
                submitAction(UGCOnboardingAction.CheckTnc)
                submitAction(UGCOnboardingAction.ClickNext)
            } andThen { state, events ->
                events.last().assertEvent(UGCOnboardingUiEvent.ShowError)

                state.isSubmit.assertFalse()
                state.hasAcceptTnc.assertFalse()
            }
        }
    }
}
