package com.tokopedia.people.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.FollowInfoUiModelBuilder
import com.tokopedia.people.model.ProfileUiModelBuilder
import com.tokopedia.people.model.ProfileWhitelistUiModelBuilder
import com.tokopedia.people.robot.UserProfileViewModelRobot
import com.tokopedia.people.util.andThen
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileType
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileWhitelistUiModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */
class UserProfileViewModelTest {

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
    private val profileWhitelistBuilder = ProfileWhitelistUiModelBuilder()

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

    private val mockHasAcceptTnc = profileWhitelistBuilder.buildHasAcceptTnc()

    @Before
    fun setUp() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId

        coEvery { mockRepo.getProfile(mockOwnUsername) } returns mockOwnProfile
        coEvery { mockRepo.getProfile(mockOtherUsername) } returns mockOtherProfile

        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
    }

    @Test
    fun `when non-login user load data successfully, it should emit data successfully with type NotLoggedIn`() {

        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockUserSession.userId } returns ""

        val robot = UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                profileInfo equalTo mockOwnProfile
                followInfo equalTo mockOwnFollow
                profileType equalTo ProfileType.NotLoggedIn
                profileWhitelist equalTo ProfileWhitelistUiModel.Empty
            }
        }
    }

    @Test
    fun `when user load own data, it should call and emit whitelist data`() {

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId
        coEvery { mockRepo.getWhitelist(mockUserId) } returns mockHasAcceptTnc

        val robot = UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                profileInfo equalTo mockOwnProfile
                followInfo equalTo mockOwnFollow
                profileType equalTo ProfileType.Self
                profileWhitelist equalTo mockHasAcceptTnc
            }
        }
    }

    @Test
    fun `when user load others profile and hasnt follow, it should emit follow status unfollow`() {

        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherNotFollow

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                profileInfo equalTo mockOtherProfile
                followInfo equalTo mockOtherNotFollow
                profileType equalTo ProfileType.OtherUser
                profileWhitelist equalTo ProfileWhitelistUiModel.Empty
            }
        }
    }

    @Test
    fun `when user load others profile and alr follow, it should emit status followed`() {

        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                profileInfo equalTo mockOtherProfile
                followInfo equalTo mockOtherFollowed
                profileType equalTo ProfileType.OtherUser
                profileWhitelist equalTo ProfileWhitelistUiModel.Empty
            }
        }
    }

    @Test
    fun `when user load others profile and failed, it should emit empty profile`() {

        coEvery { mockRepo.getProfile(any()) } throws mockException
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                profileInfo equalTo ProfileUiModel.Empty
                followInfo equalTo mockOtherFollowed
                profileType equalTo ProfileType.OtherUser
                profileWhitelist equalTo ProfileWhitelistUiModel.Empty
            }
        }
    }

    @Test
    fun `when user load follow status and failed, it should emit status not followed`() {

        coEvery { mockRepo.getProfile(any()) } returns mockOtherProfile
        coEvery { mockRepo.getFollowInfo(any()) } throws mockException

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                profileInfo equalTo mockOtherProfile
                followInfo equalTo FollowInfoUiModel.Empty
                profileType equalTo ProfileType.OtherUser
                profileWhitelist equalTo ProfileWhitelistUiModel.Empty
            }
        }
    }
}