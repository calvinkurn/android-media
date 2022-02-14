package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliatePerformanceData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateHomeViewModelTest{
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateValidateUserStatus: AffiliateValidateUserStatusUseCase = mockk()
    private val affiliateAffiliateAnnouncementUseCase : AffiliateAnnouncementUseCase = mockk()
    private val affiliateUserPerformanceUseCase: AffiliateUserPerformanceUseCase = mockk()
    private val affiliatePerformanceDataUseCase: AffiliatePerformanceDataUseCase = mockk()
    private var affiliateHomeViewModel = spyk(AffiliateHomeViewModel(userSessionInterface, affiliateValidateUserStatus,
            affiliateAffiliateAnnouncementUseCase,affiliateUserPerformanceUseCase,affiliatePerformanceDataUseCase))

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {

        coEvery { userSessionInterface.userId } returns ""
        coEvery { userSessionInterface.email } returns ""

        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
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

        affiliateHomeViewModel.getAffiliateValidateUser()

        assertEquals(affiliateHomeViewModel.getValidateUserdata().value, affiliateValidateUserData)
    }

    @Test
    fun getAffiliateValidateUserException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } throws throwable

        affiliateHomeViewModel.getAffiliateValidateUser()

        assertEquals(affiliateHomeViewModel.getErrorMessage().value, throwable)
        assertEquals(affiliateHomeViewModel.progressBar().value, false)
    }

    /**************************** userSession() *******************************************/

    @Test
    fun userSessionTest() {
        val name = "Testing"
        val profile = "Profile Testing"
        val isLoggedIn = false
        coEvery { userSessionInterface.name } returns name
        coEvery { userSessionInterface.profilePicture } returns profile
        coEvery { userSessionInterface.isLoggedIn } returns isLoggedIn


        assertEquals(affiliateHomeViewModel.getUserName(), name)
        assertEquals(affiliateHomeViewModel.getUserProfilePicture(), profile)
        assertEquals(affiliateHomeViewModel.isUserLoggedIn(), isLoggedIn)

    }
}