package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliatePerformanceData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliatePerformanceUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
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
    private val affiliatePerformanceUseCase: AffiliatePerformanceUseCase = mockk()
    private val affiliateAffiliateAnnouncementUseCase : AffiliateAnnouncementUseCase = mockk()
    var affiliateHomeViewModel = spyk(AffiliateHomeViewModel(userSessionInterface, affiliateValidateUserStatus, affiliatePerformanceUseCase,affiliateAffiliateAnnouncementUseCase))

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

    /**************************** getAffiliatePerformance() *******************************************/
    @Test
    fun getAffiliatePerformance() {
        val affiliatePerformanceData: AffiliatePerformanceData = mockk(relaxed = true)
        val item : AffiliatePerformanceData.GetAffiliateItemsPerformanceList.Data.SectionData.Item = mockk(relaxed = true)
        val sectionData = AffiliatePerformanceData.GetAffiliateItemsPerformanceList.Data.SectionData(
                null,null,null,null,null,
                arrayListOf(item),null,null)
        affiliatePerformanceData.getAffiliateItemsPerformanceList?.data?.sectionData = sectionData
        coEvery { affiliatePerformanceUseCase.affiliatePerformance(any(),any()) } returns affiliatePerformanceData

        affiliateHomeViewModel.getAffiliatePerformance(0)

        //assertEquals(affiliateHomeViewModel.getAffiliateDataItems().value, affiliateHomeViewModel.convertDataToVisitables(sectionData))
        //assertEquals(affiliateHomeViewModel.getShimmerVisibility().value, false)
    }

    @Test
    fun getAffiliatePerformanceException() {
        val throwable = Throwable("Performance Data Exception")
        coEvery { affiliatePerformanceUseCase.affiliatePerformance(any(),any()) } throws throwable

        affiliateHomeViewModel.getAffiliatePerformance(0)

        assertEquals(affiliateHomeViewModel.getErrorMessage().value, throwable)
        assertEquals(affiliateHomeViewModel.getShimmerVisibility().value, false)
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