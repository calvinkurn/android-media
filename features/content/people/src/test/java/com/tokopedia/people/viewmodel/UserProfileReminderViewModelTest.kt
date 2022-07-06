package com.tokopedia.people.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.model.*
import com.tokopedia.people.robot.UserProfileViewModelRobot
import com.tokopedia.people.util.andThen
import com.tokopedia.people.util.assertEvent
import com.tokopedia.people.util.assertTrue
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class UserProfileReminderViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: UserProfileRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    private val commonBuilder = CommonModelBuilder()
    private val profileBuilder = ProfileUiModelBuilder()
    private val followInfoBuilder = FollowInfoUiModelBuilder()
    private val mutationBuilder = MutationUiModelBuilder()

    private val mockException = commonBuilder.buildException()
    private val mockUserId = "1"
    private val mockOtherUserId = "2"
    private val mockOwnUsername = "jonathandarwin"
    private val mockOtherUsername = "yanglainlain"
    private val mockChannelId = "123"
    private val mockPosition = 1

    private val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId)
    private val mockOtherProfile = profileBuilder.buildProfile(userID = mockOtherUserId)

    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(userID = mockUserId, encryptedUserID = mockUserId, status = false)
    private val mockOtherFollowed = followInfoBuilder.buildFollowInfo(userID = mockOtherUserId, encryptedUserID = mockOtherUserId, status = true)
    private val mockOtherNotFollow = followInfoBuilder.buildFollowInfo(userID = mockOtherUserId, encryptedUserID = mockOtherUserId, status = false)

    private val mockMutationSuccess = mutationBuilder.buildSuccess()
    private val mockMutationError = mutationBuilder.buildError()

    @Before
    fun setUp() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId

        coEvery { mockRepo.getProfile(mockOwnUsername) } returns mockOwnProfile
        coEvery { mockRepo.getProfile(mockOtherUsername) } returns mockOtherProfile

        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow

        coEvery { mockRepo.updateReminder(any(), any()) } returns mockMutationSuccess
    }

    @Test
    fun `when user wants to set reminder, it should emit success event`() {

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.SaveReminderActivityResult(mockChannelId, mockPosition, true))
            } recordEvent  {
                submitAction(UserProfileAction.ClickUpdateReminder(isFromLogin = false))
            } andThen {
                last().assertEvent(UserProfileUiEvent.SuccessUpdateReminder("ignore this message", mockPosition))
            }
        }
    }

    @Test
    fun `when user wants to unset reminder, it should emit success event`() {

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.SaveReminderActivityResult(mockChannelId, mockPosition, false))
            } recordEvent  {
                submitAction(UserProfileAction.ClickUpdateReminder(isFromLogin = false))
            } andThen {
                last().assertEvent(UserProfileUiEvent.SuccessUpdateReminder("ignore this message", mockPosition))
            }
        }
    }

    @Test
    fun `when user wants to unset reminder and BE fail to update, it should emit error event`() {

        coEvery { mockRepo.updateReminder(any(), any()) } returns mockMutationError

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.SaveReminderActivityResult(mockChannelId, mockPosition, false))
            } recordEvent  {
                submitAction(UserProfileAction.ClickUpdateReminder(isFromLogin = false))
            } andThen {
                last().assertEvent(UserProfileUiEvent.ErrorUpdateReminder(mockException))
            }
        }
    }

    @Test
    fun `when user wants to unset reminder and error happen, it should emit error event`() {

        coEvery { mockRepo.updateReminder(any(), any()) } throws mockException

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.SaveReminderActivityResult(mockChannelId, mockPosition, false))
            } recordEvent  {
                submitAction(UserProfileAction.ClickUpdateReminder(isFromLogin = false))
            } andThen {
                last().assertEvent(UserProfileUiEvent.ErrorUpdateReminder(mockException))
            }
        }
    }

    @Test
    fun `when user wants to unset reminder and theres no saved reminder data, it should do anything`() {

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordEvent  {
                submitAction(UserProfileAction.ClickUpdateReminder(isFromLogin = false))
            } andThen {
                this equalTo emptyList()
            }
        }
    }

    @Test
    fun `when user wants to unset reminder and after logged in, it should load new follow status and emit success event`() {

        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherNotFollow

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(false))
                submitAction(UserProfileAction.SaveReminderActivityResult(mockChannelId, mockPosition, false))
            } recordStateAndEvent   {
                coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed

                submitAction(UserProfileAction.ClickUpdateReminder(isFromLogin = true))
            } andThen { state, events ->
                state.followInfo.status.assertTrue()
                events.last().assertEvent(UserProfileUiEvent.SuccessUpdateReminder("ignore this message", mockPosition))
            }
        }
    }
}