package com.tokopedia.people.viewmodel

import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.model.*
import com.tokopedia.people.robot.UserProfileViewModelRobot
import com.tokopedia.people.util.*
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
class UserProfileFollowViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: UserProfileRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    private val commonBuilder = CommonModelBuilder()
    private val profileBuilder = ProfileUiModelBuilder()
    private val followInfoBuilder = FollowInfoUiModelBuilder()
    private val profileWhitelistBuilder = ProfileWhitelistUiModelBuilder()
    private val mutationBuilder = MutationUiModelBuilder()

    private val mockException = commonBuilder.buildException()
    private val mockUserId = "1"
    private val mockOtherUserId = "2"
    private val mockOwnUsername = "jonathandarwin"
    private val mockOtherUsername = "yanglainlain"

    private val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId)
    private val mockOtherProfile = profileBuilder.buildProfile(userID = mockOtherUserId)

    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(userID = mockUserId, encryptedUserID = mockUserId, status = false)
    private val mockOtherFollowed = followInfoBuilder.buildFollowInfo(userID = mockOtherUserId, encryptedUserID = mockOtherUserId, status = true)
    private val mockOtherNotFollow = followInfoBuilder.buildFollowInfo(userID = mockOtherUserId, encryptedUserID = mockOtherUserId, status = false)

    private val mockNoWhitelist = profileWhitelistBuilder.buildNoWhitelist()
    private val mockWhitelist = profileWhitelistBuilder.buildOnlyWhitelist()
    private val mockHasUsername = profileWhitelistBuilder.buildHasUsername()
    private val mockHasAcceptTnc = profileWhitelistBuilder.buildHasAcceptTnc()

    private val mockMutationSuccess = mutationBuilder.buildSuccess()
    private val mockMutationError = mutationBuilder.buildError()

    @Before
    fun setUp() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId

        coEvery { mockRepo.getProfile(mockOwnUsername) } returns mockOwnProfile
        coEvery { mockRepo.getProfile(mockOtherUsername) } returns mockOtherProfile

        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow

        coEvery { mockRepo.followProfile(any()) } returns mockMutationSuccess
        coEvery { mockRepo.unFollowProfile(any()) } returns mockMutationSuccess
    }

    @Test
    fun `when user wants to follow its own profile, it wont do anything`() {

        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherNotFollow

        val robot = UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile())
            } recordState {
                submitAction(UserProfileAction.ClickFollowButton(isFromLogin = false))
            } andThen {
                followInfo equalTo mockOtherNotFollow
            }
        }
    }

    @Test
    fun `when user wants to follow but user is not logged in, it wont do anything`() {

        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockUserSession.userId } returns ""
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherNotFollow

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile())
            } recordState {
                submitAction(UserProfileAction.ClickFollowButton(isFromLogin = false))
            } andThen {
                followInfo equalTo mockOtherNotFollow
            }
        }
    }

    @Test
    fun `when user wants to follow a followed profile after login, it wont do anything`() {

        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile())
            } recordState {
                submitAction(UserProfileAction.ClickFollowButton(isFromLogin = true))
            } andThen {
                followInfo equalTo mockOtherFollowed
            }
        }
    }

    @Test
    fun `when user wants to follow an unfollowed profile, it will follow the profile`() {

        val mockOtherProfileAfterFollow = mockOtherProfile.copy(
            stats = mockOtherProfile.stats.copy(
                totalFollowerFmt = "1"
            )
        )

        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherNotFollow

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile())
            } recordState {
                coEvery { mockRepo.getProfile(mockOtherUserId) } returns mockOtherProfileAfterFollow

                submitAction(UserProfileAction.ClickFollowButton(isFromLogin = false))
            } andThen {
                followInfo.status.assertTrue()
                profileInfo equalTo mockOtherProfileAfterFollow
            }
        }
    }

    @Test
    fun `when user wants to follow an unfollowed profile and failed, it should emit error event and not change follow status`() {

        coEvery { mockRepo.followProfile(any()) } returns mockMutationError
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherNotFollow

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile())
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButton(isFromLogin = false))
            } andThen { state, events ->
                state.followInfo.status.assertFalse()
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow("ignore this message"))
            }
        }
    }

    @Test
    fun `when user wants to unfollow a followed profile, it will unfollow the profile`() {

        val mockOtherProfileAfterUnFollow = mockOtherProfile.copy(
            stats = mockOtherProfile.stats.copy(
                totalFollowerFmt = "0"
            )
        )

        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile())
            } recordState {
                coEvery { mockRepo.getProfile(mockOtherUserId) } returns mockOtherProfileAfterUnFollow

                submitAction(UserProfileAction.ClickFollowButton(isFromLogin = false))
            } andThen {
                followInfo.status.assertFalse()
                profileInfo equalTo mockOtherProfileAfterUnFollow
            }
        }
    }

    @Test
    fun `when user wants to unfollow a followed profile and failed, it should emit error event and not change follow status`() {

        coEvery { mockRepo.unFollowProfile(any()) } returns mockMutationError
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile())
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButton(isFromLogin = false))
            } andThen { state, events ->
                state.followInfo.status.assertTrue()
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow("ignore this message"))
            }
        }
    }

    @Test
    fun `when user wants to unfollow a followed profile and error happen, it should emit error event and not change follow status`() {

        coEvery { mockRepo.unFollowProfile(any()) } throws mockException
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile())
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButton(isFromLogin = false))
            } andThen { state, events ->
                state.followInfo.status.assertTrue()
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow("ignore this message"))
            }
        }
    }

    @Test
    fun `when user wants to update profile after unfollow profile and error happen, it should emit error event and still change follow status`() {

        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile())
            } recordStateAndEvent {
                coEvery { mockRepo.getProfile(mockOtherUserId) } throws mockException

                submitAction(UserProfileAction.ClickFollowButton(isFromLogin = false))
            } andThen { state, events ->
                state.followInfo.status.assertFalse()
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow("ignore this message"))
            }
        }
    }
}