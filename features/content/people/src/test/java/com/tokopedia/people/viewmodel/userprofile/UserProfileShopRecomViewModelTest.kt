package com.tokopedia.people.viewmodel.userprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.shoprecom.ShopRecomModelBuilder
import com.tokopedia.people.model.userprofile.FollowInfoUiModelBuilder
import com.tokopedia.people.model.userprofile.MutationUiModelBuilder
import com.tokopedia.people.model.userprofile.ProfileWhitelistUiModelBuilder
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

class UserProfileShopRecomViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: UserProfileRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    private val commonBuilder = CommonModelBuilder()
    private val followInfoBuilder = FollowInfoUiModelBuilder()
    private val profileWhitelistBuilder = ProfileWhitelistUiModelBuilder()
    private val shopRecomBuilder = ShopRecomModelBuilder()
    private val mutationBuilder = MutationUiModelBuilder()

    private val mockMutationSuccess = mutationBuilder.buildSuccess()
    private val mockMutationError = mutationBuilder.buildError()
    private val mockException = commonBuilder.buildException()
    private val mockHasAcceptTnc = profileWhitelistBuilder.buildHasAcceptTnc()
    private val mockShopRecomIsShown = shopRecomBuilder.buildModelIsShown()
    private val mockShopRecomIsNotShown = shopRecomBuilder.buildModelIsNotShown()
    private val mockEmptyShopRecom = shopRecomBuilder.buildEmptyModel()
    private val mockOwnUserId = "1"
    private val mockOtherUserId = "2"
    private val mockOwnUsername = "fachrizalmrsln"
    private val mockOtherUsername = "jonathandarwin"
    private val mockItemId = shopRecomBuilder.mockItemId
    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(
        userID = mockOwnUserId,
        encryptedUserID = mockOwnUserId,
        status = true
    )
    private val mockOtherNotFollow = followInfoBuilder.buildFollowInfo(
        userID = mockOtherUserId,
        encryptedUserID = mockOtherUserId,
        status = true
    )

    private val robot = UserProfileViewModelRobot(
        username = mockOwnUsername,
        repo = mockRepo,
        dispatcher = testDispatcher,
        userSession = mockUserSession,
    )

    @Before
    fun setUp() {
        coEvery { mockUserSession.userId } returns mockOwnUserId
        coEvery { mockRepo.getProfile(mockOwnUsername) }
        coEvery { mockRepo.getWhitelist() } returns mockHasAcceptTnc
    }

    @Test
    fun `when user not login, self profile and not load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockRepo.getFollowInfo(listOf(mockOtherUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockEmptyShopRecom

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                robot.viewModel.isShopRecomShow.assertFalse()
                shopRecom.items.assertEmpty()
                shopRecom equalTo mockEmptyShopRecom
            }
        }
    }

    @Test
    fun `when user not login, other profile and not load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockRepo.getFollowInfo(listOf(mockOtherUsername)) } returns mockOtherNotFollow
        coEvery { mockRepo.getShopRecom() } returns mockEmptyShopRecom

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                robot.viewModel.isShopRecomShow.assertFalse()
                shopRecom.items.assertEmpty()
                shopRecom equalTo mockEmptyShopRecom
            }
        }
    }

    @Test
    fun `when user not login, fail and not load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockRepo.getFollowInfo(any()) } throws mockException
        coEvery { mockRepo.getShopRecom() } returns mockEmptyShopRecom

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                robot.viewModel.isShopRecomShow.assertFalse()
                shopRecom.items.assertEmpty()
                shopRecom equalTo mockEmptyShopRecom
            }
        }
    }

    @Test
    fun `when user login, self profile, success load shop and isShown then it will emit the data`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomIsShown

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                robot.viewModel.isShopRecomShow.assertTrue()
                shopRecom.items.size equalTo 10
                shopRecom equalTo mockShopRecomIsShown
            }
        }
    }

    @Test
    fun `when user login, self profile, success load shop and isNotShown then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomIsNotShown

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                robot.viewModel.isShopRecomShow.assertFalse()
                shopRecom.items.assertEmpty()
                shopRecom equalTo mockEmptyShopRecom
            }
        }
    }

    @Test
    fun `when user login, self profile and fail load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } throws mockException

        robot.use {
            it.recordStateAndEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen { state, events ->
                robot.viewModel.isShopRecomShow.assertFalse()
                state.shopRecom equalTo mockEmptyShopRecom
                events.last().assertEvent(UserProfileUiEvent.ErrorLoadProfile(Throwable("any throwable")))
            }
        }
    }

    @Test
    fun `when user login, other profile and not load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOtherUsername)) } returns mockOtherNotFollow
        coEvery { mockRepo.getShopRecom() } returns mockEmptyShopRecom

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                robot.viewModel.isShopRecomShow.assertFalse()
                shopRecom.items.assertEmpty()
                shopRecom equalTo mockEmptyShopRecom
            }
        }
    }

    @Test
    fun `when user login, fail and not load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(any()) } throws mockException
        coEvery { mockRepo.getShopRecom() } returns mockEmptyShopRecom

        robot.use {
            it.recordStateAndEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen { state, events ->
                robot.viewModel.isShopRecomShow.assertFalse()
                state.shopRecom equalTo mockEmptyShopRecom
                events.last().assertEvent(UserProfileUiEvent.ErrorLoadProfile(Throwable("any throwable")))
            }
        }
    }

    @Test
    fun `when user success follow shop`() {
        val mockShopRecomIsShownAfterFollow = mockShopRecomIsShown.copy(
            items = mockShopRecomIsShown.items.map {
                if (it.id == mockItemId) it.copy(isFollow = true)
                else it
            }
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomIsShownAfterFollow
        coEvery { mockRepo.followProfile(any()) } returns mockMutationSuccess

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom equalTo mockShopRecomIsShownAfterFollow
            }
        }
    }

    @Test
    fun `when user success unfollow shop`() {
        val mockShopRecomIsShownAfterUnfollow = mockShopRecomIsShown.copy(
            items = mockShopRecomIsShown.items.map {
                if (it.id == mockItemId) it.copy(isFollow = false)
                else it
            }
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomIsShownAfterUnfollow
        coEvery { mockRepo.unFollowProfile(any()) } returns mockMutationSuccess

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom equalTo mockShopRecomIsShownAfterUnfollow
            }
        }
    }

    @Test
    fun `when user fail follow shop`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomIsShown
        coEvery { mockRepo.followProfile(any()) } returns mockMutationError

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent  {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom equalTo mockShopRecomIsShown
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow("any error"))
            }
        }
    }

    @Test
    fun `when user fail unfollow shop`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomIsShown
        coEvery { mockRepo.unFollowProfile(any()) } returns mockMutationError

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent  {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom equalTo mockShopRecomIsShown
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow("any error"))
            }
        }
    }

    @Test
    fun `when user success remove shop`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomIsShown
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.RemoveShopRecomItem(mockItemId))
            } andThen {
                shopRecom.items.size equalTo 9
            }
        }
    }

    @Test
    fun `when user fail remove shop`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomIsShown
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.RemoveShopRecomItem(123))
            } andThen {
                shopRecom.items.size equalTo 10
            }
        }
    }

}