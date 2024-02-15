package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateViewModelTest {
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateValidateUserStatus: AffiliateValidateUserStatusUseCase = mockk()
    private val getUnreadNotificationUseCase: AffiliateGetUnreadNotificationUseCase = mockk()
    private var affiliateViewModel = spyk(
        AffiliateViewModel(
            userSessionInterface,
            affiliateValidateUserStatus,
            getUnreadNotificationUseCase
        )
    )

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        coEvery { userSessionInterface.userId } returns ""
        coEvery { userSessionInterface.email } returns ""

        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** getAffiliateValidateUser() *******************************************/
    @Test
    fun getAffiliateValidateUser() {
        val affiliateValidateUserData: AffiliateValidateUserData = mockk(relaxed = true)
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } returns affiliateValidateUserData

        affiliateViewModel.getAffiliateValidateUser()

        assertEquals(affiliateViewModel.getValidateUserdata().value, affiliateValidateUserData)
    }

    @Test
    fun getAffiliateValidateUserException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } throws throwable

        affiliateViewModel.getAffiliateValidateUser()

        assertEquals(affiliateViewModel.getErrorMessage().value, throwable)
    }

    /**************************** notification *******************************************/

    @Test
    fun `successfully getting unread notification count`() {
        coEvery {
            getUnreadNotificationUseCase.getUnreadNotifications()
        } returns 5

        affiliateViewModel.fetchUnreadNotificationCount()
        assertEquals(5, affiliateViewModel.getUnreadNotificationCount().value)
    }

    @Test
    fun `should reset notification count to zero`() {
        coEvery {
            getUnreadNotificationUseCase.getUnreadNotifications()
        } returns 5

        affiliateViewModel.fetchUnreadNotificationCount()
        assertEquals(5, affiliateViewModel.getUnreadNotificationCount().value)

        affiliateViewModel.resetNotificationCount()
        assertEquals(0, affiliateViewModel.getUnreadNotificationCount().value)
    }
}
