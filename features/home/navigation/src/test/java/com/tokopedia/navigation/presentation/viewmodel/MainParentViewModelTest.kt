package com.tokopedia.navigation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.navigation.domain.GetHomeBottomNavigationUseCase
import com.tokopedia.navigation.domain.GetNewBottomNavNotificationUseCase
import com.tokopedia.navigation.domain.model.Notification
import com.tokopedia.navigation.presentation.presenter.MainParentViewModel
import com.tokopedia.navigation.util.CompletableTask
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.BottomNavItemId
import com.tokopedia.navigation_common.ui.DiscoId
import com.tokopedia.navigation_common.ui.plus
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class MainParentViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val dispatchersRule = UnconfinedTestRule()

    private val notificationUseCase = mockk<GetNewBottomNavNotificationUseCase>(relaxed = true)
    private val homeBottomNavUseCase = mockk<GetHomeBottomNavigationUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private lateinit var viewModel: MainParentViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchersRule.coroutineDispatcher)
        viewModel = MainParentViewModel(notificationUseCase, homeBottomNavUseCase, userSession)
    }

    @After
    fun tearDown() {
        clearMocks(userSession)
    }

    @Test
    fun `get notification when logged in`() {
        setLoggedIn(true)
        val expected = Notification().apply {
            totalNotif = 2
            totalCart = 60
            totalInbox = 86
            totalNotificationOnNewInbox = 16
            totalNewInbox = 560
        }
        setNotification(expected)

        viewModel.fetchNotificationData()
        val actual = viewModel.notification.getOrAwaitValue()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get notification when not logged in`() {
        setLoggedIn(false)
        val response = Notification().apply {
            totalNotif = 2
            totalCart = 60
            totalInbox = 86
            totalNotificationOnNewInbox = 16
            totalNewInbox = 560
        }
        setNotification(response)

        viewModel.fetchNotificationData()
        val actual = viewModel.notification.getOrAwaitValue()
        Assert.assertNotEquals(response, actual)
        Assert.assertNull(actual)
    }

    @Test
    fun `given failed start loading when get notification data then not get data notification`() {
        setLoggedIn(true)
        setNotificationError()

        viewModel.fetchNotificationData()
        val actual = viewModel.notification.getOrAwaitValue()
        Assert.assertNull(actual)
    }

    @Test
    fun `fetch bottom nav bar success`() {
        val expectedCache = List(4) { buildRandomBottomNavBar() }
        setDynamicBottomNavBar(expectedCache, fromCache = true)

        val expectedNetwork = List(6) { buildRandomBottomNavBar() }
        setDynamicBottomNavBar(expectedNetwork, fromCache = false)

        viewModel.fetchDynamicBottomNavBar()
        val actualCache = viewModel.dynamicBottomNav.getOrAwaitValue()
        Assert.assertEquals(expectedCache, actualCache)

        val actualNetwork = viewModel.nextDynamicBottomNav.getOrAwaitValue()
        Assert.assertEquals(expectedNetwork, actualNetwork!!.items)
    }

    @Test
    fun `fetch bottom nav bar failure`() {
        setDynamicBottomNavBarError(fromCache = true)
        setDynamicBottomNavBarError(fromCache = false)
        viewModel.fetchDynamicBottomNavBar()

        val actualCache = viewModel.dynamicBottomNav.getOrAwaitValue()
        Assert.assertNull(actualCache)

        val actualNetwork = viewModel.nextDynamicBottomNav.getOrAwaitValue()
        Assert.assertNull(actualNetwork)
    }

    @Test
    fun `test bottom navbar containing tab by type before fetching data`() {
        val expected = List(4) { buildRandomBottomNavBar() }
        setDynamicBottomNavBar(expected, fromCache = true)

        Assert.assertFalse(
            viewModel.hasTabType(BottomNavBarItemType("really random type"))
        )

        Assert.assertFalse(
            viewModel.hasTabType(expected.first().type)
        )

        Assert.assertFalse(
            viewModel.hasTabType(expected[2].type)
        )
    }

    @Test
    fun `test bottom navbar containing tab by type after fetching data`() {
        val expected = List(4) { buildRandomBottomNavBar() }
        setDynamicBottomNavBar(expected, fromCache = true)

        viewModel.fetchDynamicBottomNavBar()

        Assert.assertFalse(
            viewModel.hasTabType(BottomNavBarItemType("really random type"))
        )

        Assert.assertTrue(
            viewModel.hasTabType(expected.first().type)
        )

        Assert.assertTrue(
            viewModel.hasTabType(expected[2].type)
        )
    }

    @Test
    fun `test get bottom navbar model by unique id without fetching data`() {
        val expected = List(4) { buildRandomBottomNavBar() }
        setDynamicBottomNavBar(expected, fromCache = true)

        Assert.assertNull(
            viewModel.getModelById(BottomNavItemId("really random id"))
        )

        Assert.assertNull(
            viewModel.getModelById(expected.first().uniqueId)
        )
    }

    @Test
    fun `test get bottom navbar model by unique id after fetching data`() {
        val expected = List(4) { buildRandomBottomNavBar() }
        setDynamicBottomNavBar(expected, fromCache = true)
        viewModel.fetchDynamicBottomNavBar()

        Assert.assertEquals(
            expected.first(),
            viewModel.getModelById(expected.first().uniqueId)
        )

        Assert.assertNull(
            viewModel.getModelById(BottomNavItemId("really random id"))
        )

        Assert.assertEquals(
            expected[2],
            viewModel.getModelById(expected[2].uniqueId)
        )
    }

    private fun setLoggedIn(isLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isLoggedIn
    }

    private fun setNotification(notification: Notification) {
        coEvery { notificationUseCase(any()) } returns notification
    }

    private fun setNotificationError() {
        coEvery { notificationUseCase(any()) } throws IllegalStateException()
    }

    private fun setDynamicBottomNavBar(bottomNavBarModels: List<BottomNavBarUiModel>, fromCache: Boolean) {
        coEvery {
            homeBottomNavUseCase(GetHomeBottomNavigationUseCase.FromCache(fromCache))
        } returns CompletableTask(bottomNavBarModels) {}
    }

    private fun setDynamicBottomNavBarError(fromCache: Boolean) {
        coEvery { homeBottomNavUseCase(GetHomeBottomNavigationUseCase.FromCache(fromCache)) } throws IllegalStateException()
    }

    private fun buildRandomBottomNavBar(): BottomNavBarUiModel {
        val id = Random.nextInt(1, 100)
        return BottomNavBarUiModel(
            id = id,
            title = "Random NavBar $id",
            jumper = null,
            assets = mapOf(
                BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("navbar $id img"),
                BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("navbar $id img"),
                BottomNavBarAsset.Key.AnimActive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.LottieUrl("navbar $id lottie")
            ),
            discoId = DiscoId(id.toString()),
            type = BottomNavBarItemType(id.toString()),
            queryParams = id.toString()
        )
    }
}
