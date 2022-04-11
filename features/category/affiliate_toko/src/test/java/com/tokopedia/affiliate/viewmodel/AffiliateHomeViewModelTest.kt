package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.*
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
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
    /**************************** getAnnouncementInformation() *******************************************/
    @Test
    fun getAnnouncementInformation(){
        val affiliateAnnouncementData : AffiliateAnnouncementData = mockk(relaxed = true)
        coEvery { affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement() } returns affiliateAnnouncementData

        affiliateHomeViewModel.getAnnouncementInformation()

        assertEquals(affiliateHomeViewModel.getAffiliateAnnouncement().value,affiliateAnnouncementData)
    }

    @Test
    fun getAnnouncementValidateException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement() } throws throwable

        affiliateHomeViewModel.getAnnouncementInformation()

        assertEquals(affiliateHomeViewModel.getAffiliateErrorMessage().value, throwable)
        assertEquals(affiliateHomeViewModel.progressBar().value, false)
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
    fun getAffiliatePerformance(){
        val affiliateUserPerformaListData: AffiliateUserPerformaListItemData = mockk(relaxed = true)
        val metricData : AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics = mockk(relaxed = true)
        val defaultMetricData = AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics(
            null,"","1",null,null,null,0,null
        )
        val performData = AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData(
            null,null,null,null, arrayListOf(defaultMetricData)
        )
        affiliateUserPerformaListData.getAffiliatePerformance.data?.userData = performData
        coEvery { affiliateUserPerformanceUseCase.affiliateUserperformance(any()) } returns affiliateUserPerformaListData

        val affiliatePerformanceListData: AffiliatePerformanceListData = mockk(relaxed = true)
        val item : AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item = mockk(relaxed = true)
        val data = AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data(
            null,null,null,null,null,
            null, arrayListOf(item),null)
        affiliatePerformanceListData.getAffiliatePerformanceList?.data?.data = data
        coEvery { affiliatePerformanceDataUseCase.affiliateItemPerformanceList(any(),any()) } returns affiliatePerformanceListData

        val listResponse = affiliateHomeViewModel.convertDataToVisitables(affiliatePerformanceListData.getAffiliatePerformanceList?.data?.data,affiliateUserPerformaListData,
            PAGE_ZERO)

        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO)
        assertEquals(affiliateHomeViewModel.getAffiliateItemCount().value,0)


    }

    @Test
    fun getAffiliatePerformanceException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateUserPerformanceUseCase.affiliateUserperformance(any()) } throws throwable

        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO)

        assertEquals(affiliateHomeViewModel.getDataShimmerVisibility().value, false)
        assertEquals(affiliateHomeViewModel.getErrorMessage().value, throwable)

        affiliateHomeViewModel.getAffiliatePerformance(1)

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
    /**************************** getSelectedDate() *******************************************/
    @Test
    fun getSelectedDataTest(){
        val selectedDate = AffiliateBottomDatePicker.THIRTY_DAYS
        assertEquals(affiliateHomeViewModel.getSelectedDate(),selectedDate)
    }

    /**************************** onRangeChanged() *******************************************/
    @Test
    fun onRangeChangeTest(){
        val range : AffiliateDatePickerData = mockk(relaxed = true)
        affiliateHomeViewModel.onRangeChanged(range)

        assertEquals(affiliateHomeViewModel.getRangeChanged().value,true)
    }

}