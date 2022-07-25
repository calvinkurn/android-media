package com.tokopedia.people.viewmodel.userprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModel
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
    private val mockOwnUserId = "1"
    private val mockOtherUserId = "2"
    private val mockOwnUsername = "fachrizalmrsln"
    private val mockItemId: Long = 1353688

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

    private val mockHasAcceptTnc = profileWhitelistBuilder.buildHasAcceptTnc()
    private val mockShopRecom = shopRecomBuilder.buildModel()

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
        coEvery { mockRepo.getShopRecom() } returns mockShopRecom
    }

    @Test
    fun `when user successfully load shop recommendation and isShown is true, it will emit the data`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))

                robot.viewModel.isShopRecomShow.assertTrue()
            } andThen {
                shopRecom.items.size equalTo 10
                shopRecom equalTo mockShopRecom
            }
        }
    }

    @Test
    fun `when user successfully load shop recommendation and isShown is false, it will emit emptyList`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(any()) } returns mockOtherNotFollow

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))

                robot.viewModel.isShopRecomShow.assertFalse()
            } andThen {
                shopRecom.items.assertEmpty()
                shopRecom equalTo ShopRecomUiModel()
            }
        }
    }

    @Test
    fun `when user fail load shop recommendation and isShown is false, it will emit emptyList`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(any()) } throws mockException

        robot.use {
            it.recordStateAndEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))

                robot.viewModel.isShopRecomShow.assertFalse()
            } andThen { state, events ->
                state.shopRecom.items.assertEmpty()
                events.last()
                    .assertEvent(UserProfileUiEvent.ErrorLoadProfile(Throwable("any throwable")))
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
        coEvery { mockRepo.followProfile(any()) } returns mockMutationSuccess
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomAfterFollow

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
        coEvery { mockRepo.unFollowProfile(any()) } returns mockMutationSuccess
        coEvery { mockRepo.getShopRecom() } returns mockShopRecomAfterUnfollow

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
    fun `when user fail follow or unfollow shop`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUsername)) } returns mockOwnFollow
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

}