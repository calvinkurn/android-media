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
    private val mockShopRecom = shopRecomBuilder.buildModel()
    private val mockEmptyShopRecom = shopRecomBuilder.buildEmptyModel()
    private val mockOwnUserId = shopRecomBuilder.mockOwnUserId
    private val mockOtherUserId = shopRecomBuilder.mockOtherUserId
    private val mockOwnUsername = shopRecomBuilder.mockOwnUsername
    private val mockItemId = shopRecomBuilder.mockItemId
    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(
        userID = mockOwnUserId,
        encryptedUserID = mockOwnUserId,
        status = true
    )
    private val mockOtherNotFollow = followInfoBuilder.buildFollowInfo(
        userID = mockOtherUserId,
        encryptedUserID = mockOtherUserId,
        status = false
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
    fun `when user login, self and success load shop then isShown is true, it will emit the data`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getShopRecom() } returns mockShopRecom
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                robot.viewModel.isShopRecomShow.assertTrue()
                shopRecom.items.size equalTo 10
                shopRecom equalTo mockShopRecom
            }
        }
    }

    @Test
    fun `when user login, other and not load shop then isShown is false, it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getShopRecom() } returns mockEmptyShopRecom
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherNotFollow

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
    fun `when user login, self and fail load shop then isShown is false, it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getShopRecom() } throws mockException
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOwnFollow

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
    fun `when user login, other and fail load shop then isShown is false, it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getShopRecom() } throws mockException
        coEvery { mockRepo.getFollowInfo(any()) } throws mockException

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
    fun `when user not login and not load shop then isShown is false, it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockRepo.getShopRecom() } returns mockEmptyShopRecom
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOwnFollow

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
    fun `when user success follow shop`() {
        val mockShopRecomAfterFollow = mockShopRecom.copy(
            items = mockShopRecom.items.map {
                if (it.id == mockItemId) it.copy(isFollow = true)
                else it
            }
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomAfterFollow
        coEvery { mockRepo.followProfile(any()) } returns mockMutationSuccess

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom equalTo mockShopRecomAfterFollow
            }
        }
    }

    @Test
    fun `when user success unfollow shop`() {
        val mockShopRecomAfterUnfollow = mockShopRecom.copy(
            items = mockShopRecom.items.map {
                if (it.id == mockItemId) it.copy(isFollow = false)
                else it
            }
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomAfterUnfollow
        coEvery { mockRepo.unFollowProfile(any()) } returns mockMutationSuccess

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom equalTo mockShopRecomAfterUnfollow
            }
        }
    }

    @Test
    fun `when user fail follow shop`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecom
        coEvery { mockRepo.followProfile(any()) } returns mockMutationError

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow("any error"))
            }
        }
    }

    @Test
    fun `when user fail unfollow shop`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom() } returns mockShopRecom
        coEvery { mockRepo.unFollowProfile(any()) } returns mockMutationError

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow("any error"))
            }
        }
    }

    @Test
    fun `when user remove shop`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getShopRecom() } returns mockShopRecom
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

}