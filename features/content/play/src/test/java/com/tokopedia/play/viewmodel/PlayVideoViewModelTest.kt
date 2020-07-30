package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.Event
import com.tokopedia.play.view.viewmodel.PlayVideoViewModel
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by jegul on 07/04/20
 */
class PlayVideoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider
    private val mockPlayPreference: PlayPreference = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    private lateinit var playVideoViewModel: PlayVideoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers.main)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when not logged in, should show one tap onboarding after 5s`() = runBlockingTest {
        every { mockUserSession.isLoggedIn } returns false
        every { mockPlayPreference.isOneTapOnboardingShown(any()) } returns false

        playVideoViewModel = PlayVideoViewModel(
                dispatchers = dispatchers,
                playPreference = mockPlayPreference,
                userSession = mockUserSession
        )

        val testDispatcher = dispatchers.main as TestCoroutineDispatcher
        testDispatcher.advanceTimeBy(5000)

        val expectedResult = Event(Unit)

        Assertions.assertThat(playVideoViewModel.observableOneTapOnboarding.getOrAwaitValue())
                .isEqualToComparingFieldByField(expectedResult)
    }

    @Test
    fun `when logged in and has not shown one tap onboarding before, should show one tap onboarding after 5s`() = runBlockingTest {
        every { mockUserSession.isLoggedIn } returns true
        every { mockPlayPreference.isOneTapOnboardingShown(any()) } returns false

        playVideoViewModel = PlayVideoViewModel(
                dispatchers = dispatchers,
                playPreference = mockPlayPreference,
                userSession = mockUserSession
        )

        val testDispatcher = dispatchers.main as TestCoroutineDispatcher
        testDispatcher.advanceTimeBy(5000)

        val expectedResult = Event(Unit)

        Assertions.assertThat(playVideoViewModel.observableOneTapOnboarding.getOrAwaitValue())
                .isEqualToComparingFieldByField(expectedResult)
    }

    @Test(expected = TimeoutException::class)
    fun `when logged in and has shown one tap onboarding before, should not show one tap onboarding after 5s`() = runBlockingTest {
        every { mockUserSession.isLoggedIn } returns true
        every { mockPlayPreference.isOneTapOnboardingShown(any()) } returns true

        playVideoViewModel = PlayVideoViewModel(
                dispatchers = dispatchers,
                playPreference = mockPlayPreference,
                userSession = mockUserSession
        )

        val testDispatcher = dispatchers.main as TestCoroutineDispatcher
        testDispatcher.advanceTimeBy(5000)

        playVideoViewModel.observableOneTapOnboarding.getOrAwaitValue()
    }

}