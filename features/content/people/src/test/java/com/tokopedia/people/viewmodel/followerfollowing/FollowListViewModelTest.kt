package com.tokopedia.people.viewmodel.followerfollowing

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.people.model.FollowListModelBuilder
import com.tokopedia.people.repo.MockUserFollowRepository
import com.tokopedia.people.viewmodels.FollowListViewModel
import com.tokopedia.people.views.uimodel.FollowListType
import com.tokopedia.people.views.uimodel.action.FollowListAction
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
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
    }

    @Test
    fun `given profile id, when get followers data, should get the data correspond to the profile`() = runTestUnconfined {
        val id = "test account"
        val result = modelBuilder.getFollowersData()
        repo.setFollowersData(id, "", result)

        val viewModel = createViewModel(FollowListType.Follower, id)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FollowListAction.Init)

        val value = viewModel.uiState.value
        value.followList.assertEqualTo(result.followers)
    }

    @Test
    fun `test refresh data`() {
    }

    @Test
    fun `test load more data`() {
    }

    @Test
    fun `test follow user`() {
    }

    @Test
    fun `test follow shop`() {
    }

    @Test
    fun `test update user follow from result`() {
    }

    @Test
    fun `test update shop follow from result`() {
    }

    @Test
    fun `test consume event after handled`() {
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
