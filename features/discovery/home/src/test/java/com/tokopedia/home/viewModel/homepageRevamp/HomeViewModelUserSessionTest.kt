package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class HomeViewModelUserSessionTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when user id exist then got real user id`() {
        val userId = "1234"

        every { userSessionInterface.userId } returns userId
        homeViewModel = createHomeViewModel(userSessionInterface = userSessionInterface)

        Assert.assertEquals(homeViewModel.getUserId(), userId)
    }

    @Test
    fun `when user id null in session interface then user id empty`() {
        val userId = null

        every { userSessionInterface.userId } returns userId
        homeViewModel = createHomeViewModel(userSessionInterface = userSessionInterface)

        assert(homeViewModel.getUserId().isEmpty())
    }

    @Test
    fun `when user name exist then got real user name`() {
        val userName = "ddnokitaro"

        every { userSessionInterface.name } returns userName
        homeViewModel = createHomeViewModel(userSessionInterface = userSessionInterface)

        Assert.assertEquals(homeViewModel.getUserName(), userName)
    }

    @Test
    fun `when user name null in session interface then user name empty`() {
        val userName = null

        every { userSessionInterface.name } returns userName
        homeViewModel = createHomeViewModel(userSessionInterface = userSessionInterface)

        assert(homeViewModel.getUserName().isEmpty())
    }
}