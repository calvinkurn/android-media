package com.tokopedia.people.viewmodel.userprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.shoprecom.ShopRecomModelBuilder
import com.tokopedia.people.model.userprofile.FollowInfoUiModelBuilder
import com.tokopedia.people.model.userprofile.ProfileUiModelBuilder
import com.tokopedia.people.model.userprofile.ProfileWhitelistUiModelBuilder
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
import io.mockk.coJustRun
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
    private val shopRecomBuilder = ShopRecomModelBuilder()

    private val mockException = commonBuilder.buildException()
    private val mockUserId = "1"
    private val mockOtherUserId = "2"
    private val mockOwnUsername = "jonathandarwin"
    private val mockOtherUsername = "yanglainlain"

    private val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId)
    private val mockOtherProfile = profileBuilder.buildProfile(userID = mockOtherUserId)
    private val mockOtherBlockedProfile = profileBuilder.buildProfile(userID = mockOtherUserId, isBlocking = true)
    private val mockShopRecom = shopRecomBuilder.buildModelIsShown(nextCursor = "")
    private val mockEmptyShopRecom = shopRecomBuilder.buildEmptyModel()

    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(userID = mockUserId, encryptedUserID = mockUserId, status = false)
    private val mockOtherFollowed = followInfoBuilder.buildFollowInfo(userID = mockOtherUserId, encryptedUserID = mockOtherUserId, status = true)
    private val mockOtherNotFollow = followInfoBuilder.buildFollowInfo(userID = mockOtherUserId, encryptedUserID = mockOtherUserId, status = false)

    private val mockHasAcceptTnc = profileWhitelistBuilder.buildHasAcceptTnc()
    private val mockHasNotAcceptTnc = profileWhitelistBuilder.buildHasNotAcceptTnc()

    @Before
    fun setUp() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId

        coEvery { mockRepo.getProfile(mockOwnUsername) } returns mockOwnProfile
        coEvery { mockRepo.getProfile(mockOtherUsername) } returns mockOtherProfile

        coEvery { mockRepo.getFollowInfo(listOf(mockUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecom
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
                followInfo equalTo FollowInfoUiModel.Empty
                profileType equalTo ProfileType.NotLoggedIn
                profileWhitelist equalTo ProfileWhitelistUiModel.Empty
                shopRecom equalTo mockEmptyShopRecom
                it.viewModel.profileUserEncryptedID equalTo mockOwnProfile.encryptedUserID
            }
        }
    }

    @Test
    fun `when user load own data, it should call and emit whitelist data`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId
        coEvery { mockRepo.getWhitelist() } returns mockHasAcceptTnc

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
    fun `when user load own data, and need to onboarding`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId
        coEvery { mockRepo.getWhitelist() } returns mockHasAcceptTnc

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
                it.viewModel.needOnboarding equalTo false
            }
        }
    }

    @Test
    fun `when user load own data, and no need to onboarding`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId
        coEvery { mockRepo.getWhitelist() } returns mockHasNotAcceptTnc

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
                it.viewModel.needOnboarding equalTo true
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
                shopRecom equalTo mockEmptyShopRecom
                it.viewModel.profileUserEncryptedID equalTo mockOtherProfile.encryptedUserID
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
                shopRecom equalTo mockEmptyShopRecom
            }
        }
    }

    @Test
    fun `when user load others profile and failed, it should emit empty profile and all related data`() {
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
                followInfo equalTo FollowInfoUiModel.Empty
                profileType equalTo ProfileType.Unknown
                profileWhitelist equalTo ProfileWhitelistUiModel.Empty
                shopRecom equalTo ShopRecomUiModel()
            }
        }
    }

    @Test
    fun `when user load follow status and but hasnt logged in, it should emit status not followed`() {
        coEvery { mockRepo.getProfile(any()) } returns mockOtherProfile
        coEvery { mockRepo.getFollowInfo(any()) } throws mockException
        coEvery { mockUserSession.isLoggedIn } returns false

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
                profileType equalTo ProfileType.NotLoggedIn
                profileWhitelist equalTo ProfileWhitelistUiModel.Empty
                shopRecom equalTo mockEmptyShopRecom
            }
        }
    }

    @Test
    fun `when user block a creator and it is success, it should be blocked`() {
        coEvery { mockRepo.getProfile(any()) } returns mockOtherProfile
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed
        coEvery { mockUserSession.isLoggedIn } returns true
        coJustRun { mockRepo.blockUser(any()) }

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.BlockUser)
            } andThen {
                profileInfo.isBlocking equalTo true
            }
        }
    }

    @Test
    fun `when user block a creator and it is failed, it should not be blocked`() {
        coEvery { mockRepo.getProfile(any()) } returns mockOtherProfile
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.blockUser(any()) } throws mockException

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.BlockUser)
            } andThen {
                profileInfo.isBlocking equalTo false
            }
        }
    }

    @Test
    fun `when user unblock a creator and it is success, it should not be blocked`() {
        coEvery { mockRepo.getProfile(any()) } returns mockOtherBlockedProfile
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed
        coEvery { mockUserSession.isLoggedIn } returns true
        coJustRun { mockRepo.unblockUser(any()) }

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.UnblockUser)
            } andThen {
                profileInfo.isBlocking equalTo false
            }
        }
    }

    @Test
    fun `when user unblock a creator and it is failed, it should stay blocked`() {
        coEvery { mockRepo.getProfile(any()) } returns mockOtherBlockedProfile
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherFollowed
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.unblockUser(any()) } throws mockException

        val robot = UserProfileViewModelRobot(
            username = mockOtherUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        )

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.UnblockUser)
            } andThen {
                profileInfo.isBlocking equalTo true
            }
        }
    }
}
