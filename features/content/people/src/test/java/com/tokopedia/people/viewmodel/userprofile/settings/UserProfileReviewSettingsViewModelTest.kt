package com.tokopedia.people.viewmodel.userprofile.settings

import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.review.UserReviewModelBuilder
import com.tokopedia.people.robot.UserProfileSettingsViewModelRobot
import com.tokopedia.people.util.andThen
import com.tokopedia.people.util.assertEvent
import com.tokopedia.people.util.assertFalse
import com.tokopedia.people.util.assertTrue
import com.tokopedia.people.views.uimodel.action.UserProfileSettingsAction
import com.tokopedia.people.views.uimodel.event.UserProfileSettingsEvent
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on June 05, 2023
 */
class UserProfileReviewSettingsViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: UserProfileRepository = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val reviewModelBuilder = UserReviewModelBuilder()

    private val mockException = commonModelBuilder.buildException()
    private val mockReviewSettingsEnabled = reviewModelBuilder.buildReviewSetting(isEnabled = true)
    private val mockReviewSettingsDisabled = reviewModelBuilder.buildReviewSetting(isEnabled = false)

    @Before
    fun setUp() {
        coEvery { mockRepo.getProfileSettings(any()) } returns mockReviewSettingsEnabled
        coEvery { mockRepo.setShowReview(any(), any(), any()) } returns true
    }

    @Test
    fun `GetProfileSettings - isEnabled true`() {
        UserProfileSettingsViewModelRobot(
            repo = mockRepo,
            dispatcher = testDispatcher
        ).start {
            recordReviewSettingsState {

            } andThen {
                isEnabled.assertTrue()
            }

            viewModel.isAnySettingsChanged.assertFalse()
        }
    }

    @Test
    fun `GetProfileSettings - isEnabled false`() {
        coEvery { mockRepo.getProfileSettings(any()) } returns mockReviewSettingsDisabled

        UserProfileSettingsViewModelRobot(
            repo = mockRepo,
            dispatcher = testDispatcher
        ).start {
            recordReviewSettingsState {

            } andThen {
                isEnabled.assertFalse()
            }

            viewModel.isAnySettingsChanged.assertFalse()
        }
    }

    @Test
    fun `GetProfileSettings - network error`() {
        coEvery { mockRepo.getProfileSettings(any()) } throws mockException

        UserProfileSettingsViewModelRobot(
            repo = mockRepo,
            dispatcher = testDispatcher
        ).start {
            recordReviewSettingsState {

            } andThen {
                isEnabled.assertFalse()
            }

            viewModel.isAnySettingsChanged.assertFalse()
        }
    }

    @Test
    fun `SetShowReview - set show true success`() {

        coEvery { mockRepo.getProfileSettings(any()) } returns mockReviewSettingsDisabled

        UserProfileSettingsViewModelRobot(
            repo = mockRepo,
            dispatcher = testDispatcher
        ).start {
            recordReviewSettingsState {
                submitAction(UserProfileSettingsAction.SetShowReview(isShow = true))
            } andThen {
                isEnabled.assertTrue()
            }

            viewModel.isAnySettingsChanged.assertTrue()
        }
    }

    @Test
    fun `SetShowReview - set show false success`() {

        coEvery { mockRepo.getProfileSettings(any()) } returns mockReviewSettingsEnabled

        UserProfileSettingsViewModelRobot(
            repo = mockRepo,
            dispatcher = testDispatcher
        ).start {
            recordReviewSettingsState {
                submitAction(UserProfileSettingsAction.SetShowReview(isShow = false))
            } andThen {
                isEnabled.assertFalse()
            }

            viewModel.isAnySettingsChanged.assertTrue()
        }
    }

    @Test
    fun `SetShowReview - set show not success`() {

        coEvery { mockRepo.setShowReview(any(), any(), any()) } returns false

        UserProfileSettingsViewModelRobot(
            repo = mockRepo,
            dispatcher = testDispatcher
        ).start {
            val events = recordEvent {
                submitAction(UserProfileSettingsAction.SetShowReview(isShow = false))
            }

            events.last().assertEvent(UserProfileSettingsEvent.ErrorSetShowReview(mockException))

            viewModel.isAnySettingsChanged.assertFalse()
        }
    }

    @Test
    fun `SetShowReview - network error`() {

        coEvery { mockRepo.setShowReview(any(), any(), any()) } throws mockException

        UserProfileSettingsViewModelRobot(
            repo = mockRepo,
            dispatcher = testDispatcher
        ).start {
            val events = recordEvent {
                submitAction(UserProfileSettingsAction.SetShowReview(isShow = false))
            }

            events.last().assertEvent(UserProfileSettingsEvent.ErrorSetShowReview(mockException))

            viewModel.isAnySettingsChanged.assertFalse()
        }
    }

    @Test
    fun `SetShowReview - switch 2 times - isAnySettingsChanged false`() {

        UserProfileSettingsViewModelRobot(
            repo = mockRepo,
            dispatcher = testDispatcher
        ).start {
            recordReviewSettingsState {
                submitAction(UserProfileSettingsAction.SetShowReview(isShow = false))
            }

            viewModel.isAnySettingsChanged.assertTrue()

            recordReviewSettingsState {
                submitAction(UserProfileSettingsAction.SetShowReview(isShow = true))
            }

            viewModel.isAnySettingsChanged.assertFalse()
        }
    }
}
