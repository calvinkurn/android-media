package com.tokopedia.people.viewmodel.followerfollowing

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.content.test.dispatcher.StandardDispatcherProvider
import com.tokopedia.content.test.dispatcher.UnconfinedDispatcherProvider
import com.tokopedia.content.test.util.assertEmpty
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.content.test.util.assertFalse
import com.tokopedia.content.test.util.assertNotEqualTo
import com.tokopedia.content.test.util.assertTrue
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FollowListViewModelTest {

    @get:Rule
    val coroutineRule = UnconfinedTestRule()

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
        value.result!!.isSuccess.assertTrue()
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

    @Test
    fun `test no next page if next cursor is blank`() = runTestUnconfined {
        val id = "test account"
        val shops = List(4) { modelBuilder.createShop(it.toString()) }
        val resultWithNoCursor = modelBuilder.createFollowingData(followings = shops, nextCursor = "")
        repo.setFollowingData(id, "", resultWithNoCursor)

        val viewModel = createViewModel(FollowListType.Following, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)
        viewModel.uiState.value.hasNextPage.assertEqualTo(false)
    }

    @Test
    fun `test still has next page if next cursor is not blank`() = runTestUnconfined {
        val id = "test account"
        val shops = List(4) { modelBuilder.createShop(it.toString()) }
        val resultWithCursor = modelBuilder.createFollowingData(followings = shops, nextCursor = "test")
        repo.setFollowingData(id, "", resultWithCursor)

        val viewModel = createViewModel(FollowListType.Following, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)
        viewModel.uiState.value.hasNextPage.assertEqualTo(true)
    }

    @Test
    fun `test failed to get following list data`() = runTestUnconfined {
        val id = "test account"

        val viewModel = createViewModel(FollowListType.Following, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)
        viewModel.uiState.value.followList.assertEmpty()
        viewModel.uiState.value.result!!.isFailure.assertTrue()
    }

    @Test
    fun `test failed to get follower list data`() = runTestUnconfined {
        val id = "test account"

        val viewModel = createViewModel(FollowListType.Follower, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)
        viewModel.uiState.value.followList.assertEmpty()
        viewModel.uiState.value.result!!.isFailure.assertTrue()
    }

    @Test
    fun `test loading state should be true when still loading data`() = runTestStandard {
        val id = "test account"
        val shops = List(4) { modelBuilder.createShop(it.toString()) }
        val firstResult = modelBuilder.createFollowingData(followings = shops, nextCursor = "123")
        val secondResult = modelBuilder.createFollowingData(followings = shops, nextCursor = "")
        repo.setFollowingData(id, "", firstResult)
        repo.setFollowingData(id, "123", secondResult)

        val viewModel = createViewModel(FollowListType.Following, id, StandardDispatcherProvider())

        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)
        viewModel.uiState.value.isLoading.assertTrue()
        runCurrent()
        viewModel.uiState.value.isLoading.assertFalse()

        viewModel.onAction(FollowListAction.LoadMore)
        viewModel.uiState.value.isLoading.assertTrue()
        runCurrent()
        viewModel.uiState.value.isLoading.assertFalse()
    }

    @Test
    fun `test refreshing state should be true when data is refreshed`() = runTestStandard {
        val id = "test account"
        val shops = List(4) { modelBuilder.createShop(it.toString()) }
        val result = modelBuilder.createFollowingData(followings = shops, nextCursor = "")
        repo.setFollowingData(id, "", result)

        val viewModel = createViewModel(FollowListType.Following, id, StandardDispatcherProvider())

        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)
        viewModel.uiState.value.isRefreshing.assertFalse()
        runCurrent()
        viewModel.uiState.value.isRefreshing.assertFalse()

        viewModel.onAction(FollowListAction.Refresh)
        viewModel.uiState.value.isRefreshing.assertTrue()
        runCurrent()
        viewModel.uiState.value.isRefreshing.assertFalse()
    }

    @Test
    fun `test failed to follow shop`() = runTestUnconfined {
        val id = "test account"
        val shops = List(4) { modelBuilder.createShop(it.toString(), isFollowed = false) }
        val result = modelBuilder.createFollowingData(followings = shops)
        repo.setFollowingData(id, "", result)
        repo.setErrorFollowShop("Failed to follow")
        coEvery { userSession.isLoggedIn } returns true

        val viewModel = createViewModel(FollowListType.Following, id)

        val eventList = mutableListOf<FollowListEvent?>()
        backgroundScope.launch { viewModel.uiState.collect() }
        backgroundScope.launch { viewModel.uiEvent.toList(eventList) }

        viewModel.onAction(FollowListAction.Init)
        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(FollowListAction.Follow(viewModel.uiState.value.followList[0]))
        eventList.last().assertEqualTo(FollowListEvent.FailedFollow(true))
    }

    @Test
    fun `test failed to follow user`() = runTestUnconfined {
        val id = "test account"
        val users = List(4) { modelBuilder.createUser(it.toString(), isFollowed = false) }
        val result = modelBuilder.createFollowersData(followers = users)
        repo.setFollowersData(id, "", result)
        repo.setErrorFollowUser("Failed to follow")
        coEvery { userSession.isLoggedIn } returns true

        val viewModel = createViewModel(FollowListType.Follower, id)

        val eventList = mutableListOf<FollowListEvent?>()
        backgroundScope.launch { viewModel.uiState.collect() }
        backgroundScope.launch { viewModel.uiEvent.toList(eventList) }

        viewModel.onAction(FollowListAction.Init)
        viewModel.uiState.value.followList[0].isFollowed.assertEqualTo(false)

        viewModel.onAction(FollowListAction.Follow(viewModel.uiState.value.followList[0]))
        eventList.last().assertEqualTo(FollowListEvent.FailedFollow(true))
    }

    private fun runTestUnconfined(testBody: suspend TestScope.() -> Unit) = runTest(
        UnconfinedTestDispatcher(),
        testBody = testBody
    )

    private fun runTestStandard(testBody: suspend TestScope.() -> Unit) = runTest(
        StandardTestDispatcher(),
        testBody = testBody
    )

    private fun createViewModel(
        type: FollowListType,
        profileIdentifier: String,
        dispatchers: CoroutineDispatchers = UnconfinedDispatcherProvider()
    ): FollowListViewModel {
        return FollowListViewModel(
            type = type,
            profileIdentifier = profileIdentifier,
            userFollowRepo = repo,
            dispatchers = dispatchers,
            uiEventManager = UiEventManager(),
            userSession = userSession
        )
    }
}
