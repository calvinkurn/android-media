package com.tokopedia.people.viewmodel.userprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.content.ContentModelBuilder
import com.tokopedia.people.model.shoprecom.ShopRecomModelBuilder
import com.tokopedia.people.model.userprofile.FollowInfoUiModelBuilder
import com.tokopedia.people.model.userprofile.MutationUiModelBuilder
import com.tokopedia.people.model.userprofile.ProfileUiModelBuilder
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
    private val profileBuilder = ProfileUiModelBuilder()
    private val contentBuilder = ContentModelBuilder()

    private val mockMutationSuccess = mutationBuilder.buildSuccess()
    private val mockMutationError = mutationBuilder.buildError()
    private val mockException = commonBuilder.buildException()
    private val mockHasAcceptTnc = profileWhitelistBuilder.buildHasAcceptTnc()
    private val mockShopRecomIsShown = shopRecomBuilder.buildModelIsShown(nextCursor = "")
    private val mockShopRecomIsNotShown = shopRecomBuilder.buildModelIsShown(nextCursor = "", isShown = false)
    private val mockShopRecomIsShownNoLoadMore = shopRecomBuilder.buildModelIsShown(nextCursor = "")
    private val mockShopRecomIsShownTypeShop = shopRecomBuilder.buildModelIsShown(shopRecomBuilder.typeShop)
    private val mockShopRecomIsShownTypeBuyer = shopRecomBuilder.buildModelIsShown(shopRecomBuilder.typeBuyer)
    private val mockEmptyShopRecom = shopRecomBuilder.buildEmptyModel()
    private val mockEmptyItemShopRecom = shopRecomBuilder.buildEmptyItemModel()
    private val mockOwnUserId = "1"
    private val mockOtherUserId = "2"
    private val mockOwnUsername = "fachrizalmrsln"
    private val mockOtherUsername = "jonathandarwin"
    private val mockItemId = shopRecomBuilder.mockItemId
    private val mockEncryptedId = shopRecomBuilder.mockEncryptedId
    private val mockOwnProfile = profileBuilder.buildProfile(userID = mockOwnUserId, username = mockOwnUsername)
    private val mockOtherProfile = profileBuilder.buildProfile(userID = mockOtherUserId, username = mockOtherUsername)
    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(
        userID = mockOwnUserId,
        encryptedUserID = mockOwnUserId,
        status = true,
    )
    private val mockOtherNotFollow = followInfoBuilder.buildFollowInfo(
        userID = mockOtherUserId,
        encryptedUserID = mockOtherUserId,
        status = true,
    )
    private val mockTabsModel = contentBuilder.buildTabsModel(false)

    private val robot = UserProfileViewModelRobot(
        username = mockOwnUserId,
        repo = mockRepo,
        dispatcher = testDispatcher,
        userSession = mockUserSession,
    )

    private val shopActionFollow = ShopFollowAction.Follow
    private val shopActionUnfollow = ShopFollowAction.UnFollow

    @Before
    fun setUp() {
        coEvery { mockUserSession.userId } returns mockOwnUserId
        coEvery { mockRepo.getWhitelist() } returns mockHasAcceptTnc
        coEvery { mockRepo.getUserProfileTab(any()) } returns mockTabsModel
    }

    @Test
    fun `when user not login, self profile and not load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow

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
        coEvery { mockRepo.getProfile(mockOtherUserId) } returns mockOtherProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOtherUserId)) } returns mockOtherNotFollow

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
    fun `when user not login, fail get profile and not load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockRepo.getProfile(any()) } throws mockException

        robot.use {
            it.recordStateAndEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen { state, events ->
                robot.viewModel.isShopRecomShow.assertFalse()
                state.shopRecom.items.assertEmpty()
                state.shopRecom equalTo mockEmptyShopRecom
                events.last().assertEvent(UserProfileUiEvent.ErrorLoadProfile(Throwable("any throwable")))
            }
        }
    }

    @Test
    fun `when user login, self profile, success load shop and isNotShown then it will emit empty data`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsNotShown

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                robot.viewModel.isShopRecomShow.assertFalse()
                shopRecom equalTo mockShopRecomIsNotShown
            }
        }
    }

    @Test
    fun `when user login, self profile, success load shop and isShown then it will emit the data`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShown

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                robot.viewModel.isShopRecomShow.assertTrue()
                shopRecom.items.size equalTo mockShopRecomIsShown.items.size
                shopRecom equalTo mockShopRecomIsShown
            }
        }
    }

    @Test
    fun `when user login, self profile, success load shop and isShown then it will emit the data then success load more`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShown

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            }
            it.recordState {
                coEvery { mockRepo.getShopRecom("123") } returns mockShopRecomIsShown
                submitAction(UserProfileAction.LoadNextPageShopRecom("123"))
            } andThen {
                robot.viewModel.isShopRecomShow.assertTrue()
                shopRecom.items.size equalTo mockShopRecomIsShown.items.size * 2
            }
        }
    }

    @Test
    fun `when user login, self profile, success load shop and isShown then it will emit the data then fail load more`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow

        robot.use {
            it.setup {
                coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShown
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            }
            it.recordEvent {
                coEvery { mockRepo.getShopRecom("123") } throws mockException
                submitAction(UserProfileAction.LoadNextPageShopRecom("123"))
            } andThen {
                robot.viewModel.isShopRecomShow.assertFalse()
            }
        }
    }

    @Test
    fun `when user login, self profile, load next page shop recom`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownNoLoadMore

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.LoadNextPageShopRecom(""))
            } andThen {
                robot.viewModel.isShopRecomShow.assertTrue()
                shopRecom.items.size equalTo mockShopRecomIsShown.items.size
            }
        }
    }

    @Test
    fun `when user login, self profile, success load shop and isNotShown then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsNotShown

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
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } throws mockException

        robot.use {
            it.recordStateAndEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen { state, events ->
                state.shopRecom equalTo mockEmptyShopRecom
            }
        }
    }

    @Test
    fun `when user login, other profile and not load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOtherUserId) } returns mockOtherProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOtherUserId)) } returns mockOtherNotFollow

        robot.use {
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                shopRecom.items.assertEmpty()
                shopRecom equalTo mockEmptyShopRecom
            }
        }
    }

    @Test
    fun `when user login, fail load profile and not load shop then it will emit empty`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(any()) } throws mockException

        robot.use {
            it.recordStateAndEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen { state, events ->
                state.shopRecom equalTo mockEmptyShopRecom
                events.last().assertEvent(UserProfileUiEvent.ErrorLoadProfile(Throwable("any throwable")))
            }
        }
    }

    @Test
    fun `when user want to follow type shop but item not found`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownTypeShop

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(11253))
            } andThen {
                shopRecom.items.forEach { item ->
                    item.state equalTo ShopRecomFollowState.UNFOLLOW
                }
            }
        }
    }

    @Test
    fun `when user want to unfollow type shop but item not found`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeShop.copy(
            items = mockShopRecomIsShownTypeShop.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.FOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(11253))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user success follow type shop`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownTypeShop
        coEvery { mockRepo.shopFollowUnfollow(mockItemId.toString(), shopActionFollow) } returns mockMutationSuccess

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user success unfollow type shop`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeShop.copy(
            items = mockShopRecomIsShownTypeShop.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.FOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow
        coEvery { mockRepo.shopFollowUnfollow(mockItemId.toString(), shopActionUnfollow) } returns mockMutationSuccess

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user follow type unknown`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeShop.copy(
            items = mockShopRecomIsShownTypeShop.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.FOLLOW, type = 4)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow
        coEvery { mockRepo.shopFollowUnfollow(mockItemId.toString(), shopActionUnfollow) } returns mockMutationSuccess

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.LOADING_FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user fail follow type shop return mutation error`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownTypeShop
        coEvery { mockRepo.shopFollowUnfollow(any(), shopActionFollow) } returns mockMutationError

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom.items.forEach { item ->
                    item.state equalTo ShopRecomFollowState.UNFOLLOW
                }
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow(Throwable(mockMutationError.message)))
            }
        }
    }

    @Test
    fun `when user fail unfollow type shop then return mutation error`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeShop.copy(
            items = mockShopRecomIsShownTypeShop.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.FOLLOW)
                } else {
                    it
                }
            },
        )
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow
        coEvery { mockRepo.shopFollowUnfollow(any(), shopActionUnfollow) } returns mockMutationError

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow(Throwable(mockMutationError.message)))
            }
        }
    }

    @Test
    fun `when user fail follow type shop then throw exception`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownTypeShop
        coEvery { mockRepo.shopFollowUnfollow(any(), shopActionFollow) } throws mockException

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom.items.forEach { item ->
                    item.state equalTo ShopRecomFollowState.UNFOLLOW
                }
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow(mockException))
            }
        }
    }

    @Test
    fun `when user fail unfollow type shop then throw exception`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeShop.copy(
            items = mockShopRecomIsShownTypeShop.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.FOLLOW)
                } else {
                    it
                }
            },
        )
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow
        coEvery { mockRepo.shopFollowUnfollow(any(), shopActionUnfollow) } throws mockException

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow(mockException))
            }
        }
    }

    @Test
    fun `when user want to follow type buyer but item not found`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownTypeBuyer

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(11253))
            } andThen {
                shopRecom.items.forEach { item ->
                    item.state equalTo ShopRecomFollowState.UNFOLLOW
                }
            }
        }
    }

    @Test
    fun `when user want to unfollow type buyer but item not found`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeBuyer.copy(
            items = mockShopRecomIsShownTypeBuyer.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.FOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(11253))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user success follow type buyer`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownTypeBuyer
        coEvery { mockRepo.followProfile(mockEncryptedId) } returns mockMutationSuccess

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user success unfollow type buyer`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeBuyer.copy(
            items = mockShopRecomIsShownTypeBuyer.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.FOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow
        coEvery { mockRepo.unFollowProfile(mockEncryptedId) } returns mockMutationSuccess

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user fail follow type buyer return mutation error`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownTypeBuyer
        coEvery { mockRepo.followProfile(mockEncryptedId) } returns mockMutationError

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom.items.forEach { item ->
                    item.state equalTo ShopRecomFollowState.UNFOLLOW
                }
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow(Throwable(mockMutationError.message)))
            }
        }
    }

    @Test
    fun `when user fail unfollow type buyer then return mutation error`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeBuyer.copy(
            items = mockShopRecomIsShownTypeBuyer.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.FOLLOW)
                } else {
                    it
                }
            },
        )
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow
        coEvery { mockRepo.unFollowProfile(mockEncryptedId) } returns mockMutationError

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow(Throwable(mockMutationError.message)))
            }
        }
    }

    @Test
    fun `when user fail follow type buyer then throw exception`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownTypeBuyer
        coEvery { mockRepo.followProfile(mockEncryptedId) } throws mockException

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom.items.forEach { item ->
                    item.state equalTo ShopRecomFollowState.UNFOLLOW
                }
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow(mockException))
            }
        }
    }

    @Test
    fun `when user fail unfollow type buyer then throw exception`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeBuyer.copy(
            items = mockShopRecomIsShownTypeBuyer.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.FOLLOW)
                } else {
                    it
                }
            },
        )
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow
        coEvery { mockRepo.unFollowProfile(mockEncryptedId) } throws mockException

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordStateAndEvent {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen { state, events ->
                state.shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
                events.last().assertEvent(UserProfileUiEvent.ErrorFollowUnfollow(mockException))
            }
        }
    }

    @Test
    fun `when user success remove item from shop carousel`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShown

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.RemoveShopRecomItem(mockItemId))
            } andThen {
                val deletedItem = shopRecom.items.findLast {
                        find ->
                    find.id == mockItemId
                } ?: mockEmptyItemShopRecom
                deletedItem equalTo mockEmptyItemShopRecom
                shopRecom.items.size equalTo 9
            }
        }
    }

    @Test
    fun `when user fail remove item from shop carousel`() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShown

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.RemoveShopRecomItem(123))
            } andThen {
                shopRecom.items.size equalTo mockShopRecomIsShown.items.size
            }
        }
    }

    @Test
    fun `when user want to follow type shop but loading state is loading_follow`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeShop.copy(
            items = mockShopRecomIsShownTypeShop.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.LOADING_FOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.LOADING_FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user want to unfollow type shop but loading state is loading_follow`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeShop.copy(
            items = mockShopRecomIsShownTypeShop.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.LOADING_FOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.LOADING_FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user want to follow type shop but loading state is loading_unfollow`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeShop.copy(
            items = mockShopRecomIsShownTypeShop.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.LOADING_UNFOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.LOADING_UNFOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user want to unfollow type shop but loading state is loading_unfollow`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeShop.copy(
            items = mockShopRecomIsShownTypeShop.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.LOADING_UNFOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.LOADING_UNFOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user want to follow type buyer but loading state is loading_follow`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeBuyer.copy(
            items = mockShopRecomIsShownTypeBuyer.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.LOADING_FOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.LOADING_FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user want to unfollow type buyer but loading state is loading_follow`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeBuyer.copy(
            items = mockShopRecomIsShownTypeBuyer.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.LOADING_FOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.LOADING_FOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user want to follow type buyer but loading state is loading_unfollow`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeBuyer.copy(
            items = mockShopRecomIsShownTypeBuyer.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.LOADING_UNFOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.LOADING_UNFOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }

    @Test
    fun `when user want to unfollow type buyer but loading state is loading_unfollow`() {
        val mockShopRecomIsShownBeforeUnfollow = mockShopRecomIsShownTypeBuyer.copy(
            items = mockShopRecomIsShownTypeBuyer.items.map {
                if (it.id == mockItemId) {
                    it.copy(state = ShopRecomFollowState.LOADING_UNFOLLOW)
                } else {
                    it
                }
            },
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getProfile(mockOwnUserId) } returns mockOwnProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockOwnUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecomIsShownBeforeUnfollow

        robot.use {
            it.setup {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } recordState {
                submitAction(UserProfileAction.ClickFollowButtonShopRecom(mockItemId))
            } andThen {
                shopRecom.items.forEach { item ->
                    if (item.id == mockItemId) {
                        item.state equalTo ShopRecomFollowState.LOADING_UNFOLLOW
                    } else {
                        item.state equalTo ShopRecomFollowState.UNFOLLOW
                    }
                }
            }
        }
    }
}
