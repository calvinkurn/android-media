package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.PROJECT_ID
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.model.response.AffiliateKycDetailsData
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.model.response.AffiliateTransactionHistoryData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.usecase.AffiliateBalanceDataUseCase
import com.tokopedia.affiliate.usecase.AffiliateSearchUseCase
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.user.session.UserSession
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
class AffiliateIncomeViewModelTest{
    var affiliateIncomeViewModel = spyk(AffiliateIncomeViewModel())

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** getAffiliateBalance() *******************************************/
    @Test
    fun `Get Affiliate Balance`() {
        val affiliateBalance : AffiliateBalance = mockk(relaxed = true)

        coEvery { affiliateIncomeViewModel.affiliateBalanceDataUseCase.getAffiliateBalance() } returns affiliateBalance
        affiliateIncomeViewModel.getAffiliateBalance()

        assertEquals(affiliateIncomeViewModel.getAffiliateBalanceData().value , affiliateBalance.affiliateBalance?.data)

    }

    @Test
    fun `Get Affiliate Balance Exception`() {
        val exception = Throwable("Exception")

        coEvery { affiliateIncomeViewModel.affiliateBalanceDataUseCase.getAffiliateBalance() } throws exception

        affiliateIncomeViewModel.getAffiliateBalance()


    }

    /**************************** getKycDetails() *******************************************/
    @Test
    fun `Get Kyc Details`() {
        val kycDetails : AffiliateKycDetailsData = mockk(relaxed = true)

        coEvery { affiliateIncomeViewModel.affiliatKycHistoryUseCase.getKycInformation(PROJECT_ID) } returns kycDetails
        affiliateIncomeViewModel.getKycDetails()

        assertEquals(affiliateIncomeViewModel.getAffiliateKycData().value , kycDetails.kycProjectInfo)
        assertEquals(affiliateIncomeViewModel.getAffiliateKycLoader().value , false)

    }
    @Test
    fun getKycDetailsException(){
        val exception = Throwable("Exception")
        coEvery { affiliateIncomeViewModel.affiliatKycHistoryUseCase.getKycInformation(PROJECT_ID) } throws exception

        affiliateIncomeViewModel.getKycDetails()

        assertEquals(affiliateIncomeViewModel.getKycErrorMessage().value, exception.message)
        assertEquals(affiliateIncomeViewModel.getAffiliateKycLoader().value , false)
    }

    /**************************** getSelectedDate() *******************************************/
    @Test
    fun getSelectedDataTest(){
        val selectedDate = AffiliateBottomDatePicker.THIRTY_DAYS
        assertEquals(affiliateIncomeViewModel.getSelectedDate(),selectedDate)
    }

    /**************************** onRangeChanged() *******************************************/
    @Test
    fun onRangeChangeTest(){
        val range : AffiliateDatePickerData = mockk(relaxed = true)
        affiliateIncomeViewModel.onRangeChanged(range)

        assertEquals(affiliateIncomeViewModel.getRangeChange().value,true)
    }

    /**************************** getTransactionDetails() *******************************************/
    @Test
    fun getTransactionDetailsTest(){
        val transactionItem : AffiliateTransactionHistoryData.GetAffiliateTransactionHistory.Data.Transaction = mockk(relaxed = true)
        val list = MutableList(2){
            transactionItem
        }
        val data = AffiliateTransactionHistoryData.GetAffiliateTransactionHistory.Data("",null,false,10,1,"",1,list)
        val transaction = AffiliateTransactionHistoryData(AffiliateTransactionHistoryData.GetAffiliateTransactionHistory(data))

        coEvery { affiliateIncomeViewModel.affiliateTransactionHistoryUseCase.getAffiliateTransactionHistory(any(),any()) } returns transaction
        val response = affiliateIncomeViewModel.convertDataToVisitables(transaction.getAffiliateTransactionHistory?.data)

        affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)

        assertEquals(Gson().toJson(affiliateIncomeViewModel.getAffiliateDataItems().value),Gson().toJson(response))
        assertEquals(affiliateIncomeViewModel.getShimmerVisibility().value,true)
        assertEquals(affiliateIncomeViewModel.hasNext,false)
    }
    @Test
    fun getTransactionDetailNull(){
        val data = AffiliateTransactionHistoryData.GetAffiliateTransactionHistory.Data("",null,false,10,1,"",1,null)
        val transaction = AffiliateTransactionHistoryData(AffiliateTransactionHistoryData.GetAffiliateTransactionHistory(data))
        coEvery { affiliateIncomeViewModel.affiliateTransactionHistoryUseCase.getAffiliateTransactionHistory(any(),any()) } returns transaction
        val response = affiliateIncomeViewModel.convertDataToVisitables(transaction.getAffiliateTransactionHistory?.data)

        affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)

        assertEquals(affiliateIncomeViewModel.getAffiliateDataItems().value,response)
    }
@Test
    fun getTransactionDetailsTestExaception(){
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateIncomeViewModel.affiliateTransactionHistoryUseCase.getAffiliateTransactionHistory(any(),any()) } throws throwable

        affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)

        assertEquals(affiliateIncomeViewModel.getShimmerVisibility().value, false)
        assertEquals(affiliateIncomeViewModel.getErrorMessage().value , throwable)
    }

    /**************************** testAffiliateUserBlackListed() *******************************************/
    @Test
    fun testAffiliateUserBlackListed() {
        affiliateIncomeViewModel.setBlacklisted(false)
        assertEquals(affiliateIncomeViewModel.getIsBlackListed(), false)

    }

    /**************************** getAffiliateValidateUser() *******************************************/
    @Test
    fun getAffiliateValidateUser() {
        val affiliateValidateUserData: AffiliateValidateUserData = mockk(relaxed = true)
        coEvery { affiliateIncomeViewModel.affiliateValidateUseCaseUseCase.validateUserStatus(any()) } returns affiliateValidateUserData

        affiliateIncomeViewModel.getAffiliateValidateUser("")

        assertEquals(affiliateIncomeViewModel.getValidateUserdata().value, affiliateValidateUserData)
    }

    @Test
    fun getAffiliateValidateUserException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateIncomeViewModel.affiliateValidateUseCaseUseCase.validateUserStatus(any()) } throws throwable

        affiliateIncomeViewModel.getAffiliateValidateUser("")

        assertEquals(affiliateIncomeViewModel.getErrorMessage().value, throwable)
    }
    /**************************** getAnnouncementInformation() *******************************************/
    @Test
    fun getAnnouncementInformation(){
        val affiliateAnnouncementData : AffiliateAnnouncementDataV2 = mockk(relaxed = true)
        coEvery { affiliateIncomeViewModel.affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY) } returns affiliateAnnouncementData

        affiliateIncomeViewModel.getAnnouncementInformation()

        assertEquals(affiliateIncomeViewModel.getAffiliateAnnouncement().value,affiliateAnnouncementData)
    }

    @Test
    fun getAnnouncementValidateException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateIncomeViewModel.affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(
            PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY
        ) } throws throwable

        affiliateIncomeViewModel.getAnnouncementInformation()

    }
}