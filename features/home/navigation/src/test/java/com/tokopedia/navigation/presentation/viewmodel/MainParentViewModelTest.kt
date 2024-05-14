package com.tokopedia.navigation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.navigation.domain.GetHomeBottomNavigationUseCase
import com.tokopedia.navigation.domain.GetNewBottomNavNotificationUseCase
import com.tokopedia.navigation.domain.model.Notification
import com.tokopedia.navigation.presentation.presenter.MainParentViewModel
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
    fun `get notification`() {
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
    fun `given failed start loading when get notification data then not get data notification`() {
        setLoggedIn(true)
        val expected = null
        setNotificationError()

        viewModel.fetchNotificationData()
        val actual = viewModel.notification.getOrAwaitValue()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `fetch bottom nav bar success`() {
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
}
