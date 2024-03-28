package com.tokopedia.people.viewmodel.followerfollowing

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.content.test.util.assertNotEqualTo
import com.tokopedia.people.model.FollowListModelBuilder
import com.tokopedia.people.repo.MockUserFollowRepository
import com.tokopedia.people.viewmodels.FollowListViewModel
import com.tokopedia.people.views.uimodel.FollowListType
import com.tokopedia.people.views.uimodel.action.FollowListAction
import com.tokopedia.people.views.uimodel.id
import com.tokopedia.people.views.uimodel.isFollowed
import com.tokopedia.people.views.uimodel.state.FollowListEvent
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FollowListViewModelTest {

    @get:Rule
    val coroutineRule = UnconfinedTestRule()

    private val dispatcherProvider = object : CoroutineDispatchers {

        private val dispatcher = UnconfinedTestDispatcher()

        override val main: CoroutineDispatcher = dispatcher
        override val io: CoroutineDispatcher = dispatcher
        override val default: CoroutineDispatcher = dispatcher
        override val immediate: CoroutineDispatcher = dispatcher
        override val computation: CoroutineDispatcher = dispatcher
    }
    private val repo = MockUserFollowRepository()
    private val modelBuilder = FollowListModelBuilder()
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        repo.clear()
        clearMocks(userSession)
    }

    @Test
    fun `given profile id, when get followers data, should get the data correspond to the profile`() = runTestUnconfined {
        val id = "test account"
        val result = modelBuilder.getFollowersData()
        val wrongResult = modelBuilder.getFollowersData()
        repo.setFollowersData(id, "", result)
        repo.setFollowersData("wrong id", "", wrongResult)

        val viewModel = createViewModel(FollowListType.Follower, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        val value = viewModel.uiState.value
        value.followList.assertEqualTo(result.followers)
        value.followList.assertNotEqualTo(wrongResult.followers)
    }

    @Test
    fun `given profile id, when get followers data, should not get the following data`() = runTestUnconfined {
        val id = "test account"
        val followersResult = modelBuilder.getFollowersData()
        val followingResult = modelBuilder.getFollowingData()
        repo.setFollowersData(id, "", followersResult)
        repo.setFollowingData(id, "", followingResult)

        val viewModel = createViewModel(FollowListType.Follower, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        val value = viewModel.uiState.value
        value.followList.assertEqualTo(followersResult.followers)
        value.followList.assertNotEqualTo(followingResult.followingList)
    }

    @Test
    fun `given profile id, when get following data, should get the data correspond to the profile`() = runTestUnconfined {
        val id = "test account"
        val result = modelBuilder.getFollowingData()
        val wrongResult = modelBuilder.getFollowingData()
        repo.setFollowingData(id, "", result)
        repo.setFollowingData("wrong id", "", wrongResult)

        val viewModel = createViewModel(FollowListType.Following, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        val value = viewModel.uiState.value
        value.followList.assertEqualTo(result.followingList)
        value.followList.assertNotEqualTo(wrongResult.followingList)
    }

    @Test
    fun `given profile id, when get following data, should not get the followers data`() = runTestUnconfined {
        val id = "test account"
        val followingResult = modelBuilder.getFollowingData()
        val followersResult = modelBuilder.getFollowersData()
        repo.setFollowingData(id, "", followingResult)
        repo.setFollowersData(id, "", followersResult)

        val viewModel = createViewModel(FollowListType.Following, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        val value = viewModel.uiState.value
        value.followList.assertEqualTo(followingResult.followingList)
        value.followList.assertNotEqualTo(followersResult.followers)
    }

    @Test
    fun `test refresh data for following type`() = runTestUnconfined {
        val id = "test account"
        val firstResult = modelBuilder.getFollowingData()
        val afterRefreshResult = modelBuilder.getFollowingData()
        repo.setFollowingData(id, "", firstResult)

        val viewModel = createViewModel(FollowListType.Following, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        viewModel.uiState.value.followList.assertEqualTo(firstResult.followingList)

        repo.setFollowingData(id, "", afterRefreshResult)
        viewModel.onAction(FollowListAction.Refresh)

        viewModel.uiState.value.followList.assertEqualTo(afterRefreshResult.followingList)
        viewModel.uiState.value.followList.assertNotEqualTo(firstResult.followingList)
    }

    @Test
    fun `test refresh data for followers type`() = runTestUnconfined {
        val id = "test account"
        val firstResult = modelBuilder.getFollowersData()
        val afterRefreshResult = modelBuilder.getFollowersData()
        repo.setFollowersData(id, "", firstResult)

        val viewModel = createViewModel(FollowListType.Follower, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        viewModel.uiState.value.followList.assertEqualTo(firstResult.followers)

        repo.setFollowersData(id, "", afterRefreshResult)
        viewModel.onAction(FollowListAction.Refresh)

        viewModel.uiState.value.followList.assertEqualTo(afterRefreshResult.followers)
        viewModel.uiState.value.followList.assertNotEqualTo(firstResult.followers)
    }

    @Test
    fun `test load more data`() = runTestUnconfined {
        val id = "test account"
        val nextCursor = "123"
        val firstResult = modelBuilder.getFollowersData(nextCursor = nextCursor)
        val nextResult = modelBuilder.getFollowersData()
        repo.setFollowersData(id, "", firstResult)
        repo.setFollowersData(id, nextCursor, nextResult)

        val viewModel = createViewModel(FollowListType.Follower, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        viewModel.uiState.value.followList.assertEqualTo(firstResult.followers)

        viewModel.onAction(FollowListAction.LoadMore)

        viewModel.uiState.value.followList.assertEqualTo(firstResult.followers + nextResult.followers)
    }

    @Test
    fun `test follow and unfollow user when logged in`() = runTestUnconfined {
        val id = "test account"
        val users = List(4) { modelBuilder.createUser(it.toString(), isFollowed = false, isMySelf = false) }
        val result = modelBuilder.createFollowersData(followers = users)
        repo.setFollowersData(id, "", result)
        coEvery { userSession.isLoggedIn } returns true

        val viewModel = createViewModel(FollowListType.Follower, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(FollowListAction.Follow(viewModel.uiState.value.followList[0]))

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(true)

        viewModel.onAction(FollowListAction.Follow(viewModel.uiState.value.followList[0]))

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        repo.setErrorFollowUser("Cannot follow/unfollow user")

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)
    }

    @Test
    fun `test follow and unfollow shop when logged in`() = runTestUnconfined {
        val id = "test account"
        val shops = List(4) { modelBuilder.createShop(it.toString(), isFollowed = false) }
        val result = modelBuilder.createFollowingData(followings = shops)
        repo.setFollowingData(id, "", result)
        coEvery { userSession.isLoggedIn } returns true

        val viewModel = createViewModel(FollowListType.Following, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(FollowListAction.Follow(viewModel.uiState.value.followList[0]))

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(true)

        viewModel.onAction(FollowListAction.Follow(viewModel.uiState.value.followList[0]))

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        repo.setErrorFollowShop("Cannot follow/unfollow shop")

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)
    }

    @Test
    fun `test follow user when not logged in`() = runTestUnconfined {
        val id = "test account"
        val users = List(4) { modelBuilder.createUser(it.toString(), isFollowed = false, isMySelf = false) }
        val result = modelBuilder.createFollowersData(followers = users)
        repo.setFollowersData(id, "", result)

        val viewModel = createViewModel(FollowListType.Follower, id)

        val eventList = mutableListOf<FollowListEvent?>()
        backgroundScope.launch { viewModel.uiState.collect() }
        backgroundScope.launch { viewModel.uiEvent.toList(eventList) }

        viewModel.onAction(FollowListAction.Init)

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(FollowListAction.Follow(viewModel.uiState.value.followList[0]))

        eventList.last().assertEqualTo(
            FollowListEvent.LoginToFollow(viewModel.uiState.value.followList[0])
        )
    }

    @Test
    fun `test follow shop when not logged in`() = runTestUnconfined {
        val id = "test account"
        val shops = List(4) { modelBuilder.createShop(it.toString(), isFollowed = false) }
        val result = modelBuilder.createFollowingData(followings = shops)
        repo.setFollowingData(id, "", result)

        val viewModel = createViewModel(FollowListType.Following, id)

        val eventList = mutableListOf<FollowListEvent?>()
        backgroundScope.launch { viewModel.uiState.collect() }
        backgroundScope.launch { viewModel.uiEvent.toList(eventList) }

        viewModel.onAction(FollowListAction.Init)

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(FollowListAction.Follow(viewModel.uiState.value.followList[0]))

        eventList.last().assertEqualTo(
            FollowListEvent.LoginToFollow(viewModel.uiState.value.followList[0])
        )
    }

    @Test
    fun `test force update user follow state from result`() = runTestUnconfined {
        val id = "test account"
        val users = List(4) { modelBuilder.createUser(it.toString(), isFollowed = false) }
        val result = modelBuilder.createFollowersData(followers = users)
        repo.setFollowersData(id, "", result)

        val viewModel = createViewModel(FollowListType.Follower, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(
            FollowListAction.UpdateUserFollowFromResult(viewModel.uiState.value.followList[0].id, true)
        )

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(true)

        viewModel.onAction(
            FollowListAction.UpdateUserFollowFromResult(viewModel.uiState.value.followList[0].id, false)
        )

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(
            FollowListAction.UpdateShopFollowFromResult(viewModel.uiState.value.followList[0].id, true)
        )

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)
    }

    @Test
    fun `test force update shop follow state from result`() = runTestUnconfined {
        val id = "test account"
        val shops = List(4) { modelBuilder.createShop(it.toString(), isFollowed = false) }
        val result = modelBuilder.createFollowingData(followings = shops)
        repo.setFollowingData(id, "", result)

        val viewModel = createViewModel(FollowListType.Following, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(
            FollowListAction.UpdateShopFollowFromResult(viewModel.uiState.value.followList[0].id, true)
        )

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(true)

        viewModel.onAction(
            FollowListAction.UpdateShopFollowFromResult(viewModel.uiState.value.followList[0].id, false)
        )

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(
            FollowListAction.UpdateUserFollowFromResult(viewModel.uiState.value.followList[0].id, true)
        )

        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)
    }

    @Test
    fun `test consume event after handled`() = runTestUnconfined {
        val id = "test account"
        val shops = List(4) { modelBuilder.createShop(it.toString(), isFollowed = false) }
        val result = modelBuilder.createFollowingData(followings = shops)
        repo.setFollowingData(id, "", result)

        val viewModel = createViewModel(FollowListType.Following, id)

        val eventList = mutableListOf<FollowListEvent?>()
        backgroundScope.launch { viewModel.uiState.collect() }
        backgroundScope.launch { viewModel.uiEvent.toList(eventList) }

        viewModel.onAction(FollowListAction.Init)

        viewModel.onAction(FollowListAction.Follow(viewModel.uiState.value.followList[0]))

        val lastEvent = eventList.last()
        lastEvent.assertEqualTo(FollowListEvent.LoginToFollow(viewModel.uiState.value.followList[0]))

        viewModel.onAction(FollowListAction.ConsumeEvent(lastEvent!!))

        eventList.last().assertEqualTo(null)
    }

    private fun runTestUnconfined(testBody: suspend TestScope.() -> Unit) = runTest(
        UnconfinedTestDispatcher(),
        testBody = testBody
    )

    private fun createViewModel(
        type: FollowListType,
        profileIdentifier: String
    ): FollowListViewModel {
        return FollowListViewModel(
            type = type,
            profileIdentifier = profileIdentifier,
            userFollowRepo = repo,
            dispatchers = dispatcherProvider,
            uiEventManager = UiEventManager(),
            userSession = userSession
        )
    }
}
